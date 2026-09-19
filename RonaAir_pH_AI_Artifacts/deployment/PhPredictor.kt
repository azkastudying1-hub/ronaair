
package com.ronair.ph

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.*
import org.json.JSONObject

/**
 * RonaAir pH estimation from a pH-strip photo.
 *
 * This is a direct port of the Python pipeline in RonaAir_pH_Strip_AI_Colab.ipynb.
 * Feature order, colour conversions and scaling MUST stay identical to
 * deployment/preprocessing_config.json, or predictions will silently drift.
 */
object PhPredictor {

    // ---- 1. ROI ------------------------------------------------------------
    // Port of roi_fallback_segmentation(): most saturated blob, shrunk 20% toward its centre.
    // REPLACE THIS FUNCTION if RonaAir ships a different ROI detector,
    // then re-extract features and re-train the model.
    fun extractPHStripROI(bitmap: Bitmap): Bitmap {
        val w = bitmap.width; val h = bitmap.height
        val px = IntArray(w * h)
        bitmap.getPixels(px, 0, w, 0, 0, w, h)

        var minX = w; var minY = h; var maxX = 0; var maxY = 0; var found = false
        val hsv = FloatArray(3)
        for (y in 0 until h) for (x in 0 until w) {
            val c = px[y * w + x]
            Color.colorToHSV(c, hsv)
            val s = hsv[1] * 255f
            val v = hsv[2] * 255f
            if (s > 60f && v > 40f && v < 250f) {
                found = true
                if (x < minX) minX = x; if (x > maxX) maxX = x
                if (y < minY) minY = y; if (y > maxY) maxY = y
            }
        }
        if (!found) {   // central-crop fallback
            val bw = max(1, w / 6); val bh = max(1, h / 12)
            return Bitmap.createBitmap(bitmap, (w - bw) / 2, (h - bh) / 2, bw, bh)
        }
        var bw = maxX - minX + 1; var bh = maxY - minY + 1
        val x0 = minX + (0.2 * bw).toInt(); val y0 = minY + (0.2 * bh).toInt()
        bw = max(1, (0.6 * bw).toInt()); bh = max(1, (0.6 * bh).toInt())
        return Bitmap.createBitmap(bitmap, x0, y0,
            min(bw, w - x0), min(bh, h - y0))
    }

    // ---- 2. Quality gate ---------------------------------------------------
    data class Quality(val ok: Boolean, val status: String, val reasons: List<String>,
                       val brightness: Double, val blurScore: Double)

    fun checkQuality(bitmap: Bitmap, roi: Bitmap): Quality {
        val w = bitmap.width; val h = bitmap.height
        val px = IntArray(w * h); bitmap.getPixels(px, 0, w, 0, 0, w, h)
        val grey = DoubleArray(w * h)
        for (i in px.indices) {
            val c = px[i]
            grey[i] = 0.299 * Color.red(c) + 0.587 * Color.green(c) + 0.114 * Color.blue(c)
        }
        val mean = grey.average()
        val sd = sqrt(grey.sumOf { (it - mean) * (it - mean) } / grey.size)

        // 3x3 Laplacian variance, matching cv2.Laplacian(..., CV_64F).var()
        val lap = ArrayList<Double>(max(1, (w - 2) * (h - 2)))
        for (y in 1 until h - 1) for (x in 1 until w - 1) {
            val v = -4 * grey[y * w + x] + grey[(y - 1) * w + x] + grey[(y + 1) * w + x] +
                    grey[y * w + x - 1] + grey[y * w + x + 1]
            lap.add(v)
        }
        val lm = if (lap.isEmpty()) 0.0 else lap.average()
        val blur = if (lap.isEmpty()) 0.0 else lap.sumOf { (it - lm) * (it - lm) } / lap.size

        val reasons = ArrayList<String>()
        if (mean < 25.0) reasons.add("image_too_dark")
        if (mean > 245.0) reasons.add("image_overexposed")
        if (sd < 8.0) reasons.add("low_contrast")
        if (blur < 15.0) reasons.add("blurry")
        if (roi.width * roi.height < 80) reasons.add("roi_too_small")

        val hard = setOf("image_too_dark", "image_overexposed", "roi_too_small")
        val status = when {
            reasons.any { it in hard } -> "REJECT"
            reasons.isNotEmpty()       -> "WARN"
            else                       -> "GOOD"
        }
        return Quality(status != "REJECT", status, reasons, mean, blur)
    }

    // ---- 3. Colour features -----------------------------------------------
    // Must produce EXACTLY the names and order in feature_schema.json.
    fun colorFeatures(roi: Bitmap): Map<String, Double> {
        val n = roi.width * roi.height
        val px = IntArray(n); roi.getPixels(px, 0, roi.width, 0, 0, roi.width, roi.height)

        val names = listOf("R", "G", "B", "rn", "gn", "bn", "H", "S", "V", "L", "a", "blab")
        val vals = Array(names.size) { DoubleArray(n) }
        val hsv = FloatArray(3)
        for (i in 0 until n) {
            val c = px[i]
            val r = Color.red(c).toDouble(); val g = Color.green(c).toDouble()
            val b = Color.blue(c).toDouble()
            val tot = r + g + b + 1e-6
            Color.colorToHSV(c, hsv)
            val lab = rgbToLab8(r, g, b)      // OpenCV 8-bit LAB convention
            val v = doubleArrayOf(r, g, b, r / tot, g / tot, b / tot,
                hsv[0] / 2.0, hsv[1] * 255.0, hsv[2] * 255.0,   // H in 0..179 like OpenCV
                lab[0], lab[1], lab[2])
            for (k in names.indices) vals[k][i] = v[k]
        }
        val out = HashMap<String, Double>()
        for (k in names.indices) {
            val m = vals[k].average()
            val s = sqrt(vals[k].sumOf { (it - m) * (it - m) } / n)
            out["${names[k]}_mean"] = m
            out["${names[k]}_std"] = s
        }
        return out
    }

    /** sRGB -> CIELAB in OpenCV's 8-bit encoding: L*255/100, a+128, b+128. */
    private fun rgbToLab8(r: Double, g: Double, b: Double): DoubleArray {
        fun inv(c: Double): Double {
            val v = c / 255.0
            return if (v > 0.04045) ((v + 0.055) / 1.055).pow(2.4) else v / 12.92
        }
        val rl = inv(r); val gl = inv(g); val bl = inv(b)
        var x = (0.412453 * rl + 0.357580 * gl + 0.180423 * bl) / 0.950456
        var y =  0.212671 * rl + 0.715160 * gl + 0.072169 * bl
        var z = (0.019334 * rl + 0.119193 * gl + 0.950227 * bl) / 1.088754
        fun f(t: Double) = if (t > 0.008856) t.pow(1.0 / 3.0) else 7.787 * t + 16.0 / 116.0
        val fx = f(x); val fy = f(y); val fz = f(z)
        val L = 116.0 * fy - 16.0
        val A = 500.0 * (fx - fy)
        val B = 200.0 * (fy - fz)
        return doubleArrayOf(L * 255.0 / 100.0, A + 128.0, B + 128.0)
    }

    // ---- 4a. Native inference (linear / polynomial-ridge export) -----------
    // Reads deployment/model_coefficients.json. No ML runtime needed.
    class NativeModel(json: String) {
        private val spec = JSONObject(json)
        private val order: List<String> = spec.getJSONArray("feature_order").let { a ->
            (0 until a.length()).map { a.getString(it) } }
        private val coef: DoubleArray = spec.getJSONArray("coefficients").let { a ->
            DoubleArray(a.length()) { a.getDouble(it) } }
        private val intercept = spec.getDouble("intercept")
        private val mean = spec.optJSONObject("scaler")?.getJSONArray("mean")?.let { a ->
            DoubleArray(a.length()) { a.getDouble(it) } }
        private val scale = spec.optJSONObject("scaler")?.getJSONArray("scale")?.let { a ->
            DoubleArray(a.length()) { a.getDouble(it) } }
        private val powers: Array<DoubleArray>? = spec.optJSONObject("polynomial")
            ?.getJSONArray("powers")?.let { p ->
                Array(p.length()) { j ->
                    val row = p.getJSONArray(j)
                    DoubleArray(row.length()) { i -> row.getDouble(i) }
                }
            }

        fun predict(features: Map<String, Double>): Float {
            var x = DoubleArray(order.size) {
                features[order[it]] ?: throw IllegalArgumentException("missing feature ${order[it]}")
            }
            if (mean != null && scale != null)
                x = DoubleArray(x.size) { (x[it] - mean[it]) / scale[it] }
            val terms = powers?.map { p ->
                var t = 1.0
                for (i in x.indices) t *= x[i].pow(p[i])
                t
            }?.toDoubleArray() ?: x
            var s = intercept
            for (i in coef.indices) s += coef[i] * terms[i]
            return s.toFloat()
        }
    }

    // ---- 4b. TFLite inference (Keras MLP export) --------------------------
    // Requires org.tensorflow:tensorflow-lite and deployment/final_model.tflite +
    // the scaler mean/scale from preprocessing_config.json.
    //
    // val interpreter = Interpreter(loadModelFile(context, "final_model.tflite"))
    // val input = Array(1) { FloatArray(featureOrder.size) { i ->
    //     ((features[featureOrder[i]]!! - mean[i]) / scale[i]).toFloat() } }
    // val output = Array(1) { FloatArray(1) }
    // interpreter.run(input, output)
    // return output[0][0]

    // ---- 5. Entry point ----------------------------------------------------
    data class PhResult(val estimatedPh: Float?, val quality: String, val warnings: List<String>)

    fun predictPH(bitmap: Bitmap, model: NativeModel,
                  phMin: Float, phMax: Float): PhResult {
        val roi = extractPHStripROI(bitmap)
        val q = checkQuality(bitmap, roi)
        if (!q.ok) return PhResult(null, q.status, q.reasons)
        val features = colorFeatures(roi)
        val ph = model.predict(features)
        val warn = ArrayList<String>(q.reasons)
        if (ph < phMin - 0.5f || ph > phMax + 0.5f)
            warn.add("This image may be outside the model's validated pH range ($phMin-$phMax).")
        return PhResult(ph, q.status, warn)
    }
}
