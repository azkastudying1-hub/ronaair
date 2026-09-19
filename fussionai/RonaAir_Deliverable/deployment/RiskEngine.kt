// ============================================================================
// RonaAir — RiskEngine.kt
// Dibangkitkan otomatis oleh RonaAir_Decision_Fusion_Risk_AI.ipynb
// Generated at : 2026-09-18 01:24:58
// Model version: ronair-risk-ai-1.0.0
// Sumber rule  : configs/risk_rules.json  (JANGAN diedit manual; edit JSON-nya lalu regenerate)
//
// CAKUPAN:
//   Ini adalah LEVEL 1 Rule-Based Risk Engine — jalur keputusan PRODUKSI RonaAir.
//   Berjalan 100% offline, tanpa runtime Python, tanpa TensorFlow Lite, tanpa jaringan.
//
// BUKAN:
//   Bukan detektor spesies alga, bukan pengukur toksin, bukan pengganti laboratorium,
//   bukan sistem medis. Seluruh threshold berstatus PROVISIONAL.
//
// CATATAN do_est:
//   do_est adalah ESTIMATED DO dari soft-sensor. UI WAJIB menampilkannya sebagai
//   "Estimated DO", tidak pernah "Measured DO".
// ============================================================================

package id.ronaair.risk

enum class RiskLevel(val severity: Int) {
    NORMAL(0), WASPADA(1), SIAGA(2), DARURAT(3)
}

enum class DataQuality { OK, DEGRADED, POSSIBLE_OUT_OF_DISTRIBUTION, INSUFFICIENT_EVIDENCE }

data class RiskInput(
    val visualScore: Float?,
    val phVisualEst: Float?,
    val phSensor: Float?,
    val waterTemp: Float?,
    val ecValue: Float?,
    val tdsPpm: Float?,
    val doEst: Float?,
    val hour: Int?,
    val imageQuality: String? = null,
    val visualCondition: String? = null
)

data class RiskOutput(
    val status: String,
    val factors: List<String>,
    val recommendations: List<String>,
    val dataQuality: String,
    val firedRules: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val recheckAfterMinutes: Int? = null,
    val modelVersion: String = "ronair-risk-ai-1.0.0",
    val decisionSource: String = "RULE_ENGINE_LEVEL_1"
)

private data class Rule(
    val id: String, val parameter: String, val op: String,
    val threshold: Float, val unit: String, val level: RiskLevel,
    val validationStatus: String
)

private data class Recommendation(
    val id: String, val action: String, val priority: Int,
    val contraindication: String, val recheckAfterMinutes: Int?,
    val validationStatus: String
)

private data class TriggerRecommendation(
    val id: String, val triggerRules: Set<String>, val action: String,
    val contraindication: String, val validationStatus: String
)

object RiskEngine {

    private val RULES = listOf(
        Rule("R001", "do_est", "<", 2.0f, "mg/L", RiskLevel.DARURAT, "PROVISIONAL"),
        Rule("R002", "do_est", "<", 3.0f, "mg/L", RiskLevel.SIAGA, "PROVISIONAL"),
        Rule("R003", "do_est", "<", 5.0f, "mg/L", RiskLevel.WASPADA, "PROVISIONAL"),
        Rule("R010", "ph_sensor", "<", 5.5f, "pH", RiskLevel.DARURAT, "PROVISIONAL"),
        Rule("R011", "ph_sensor", ">", 9.5f, "pH", RiskLevel.DARURAT, "PROVISIONAL"),
        Rule("R012", "ph_sensor", "<", 6.5f, "pH", RiskLevel.SIAGA, "PROVISIONAL"),
        Rule("R013", "ph_sensor", ">", 9.0f, "pH", RiskLevel.SIAGA, "PROVISIONAL"),
        Rule("R020", "water_temp", ">", 34.0f, "degC", RiskLevel.SIAGA, "PROVISIONAL"),
        Rule("R021", "water_temp", "<", 20.0f, "degC", RiskLevel.WASPADA, "PROVISIONAL"),
        Rule("R030", "visual_score", ">=", 0.75f, "skor 0-1", RiskLevel.SIAGA, "PROVISIONAL"),
        Rule("R031", "visual_score", ">=", 0.5f, "skor 0-1", RiskLevel.WASPADA, "PROVISIONAL"),
        Rule("R040", "ph_difference", ">", 1.0f, "pH", RiskLevel.WASPADA, "PROVISIONAL"),
    )

    /** Rentang fisik. Di luar ini = sensor fault, bukan kondisi air ekstrem. */
    private val PLAUSIBLE: Map<String, Pair<Float, Float>> = mapOf(
        "ph_sensor" to (0.0f to 14.0f),
        "water_temp" to (0.0f to 45.0f),
        "do_est" to (0.0f to 25.0f),
        "ec_value" to (0.0f to 100000.0f),
        "tds_ppm" to (0.0f to 50000.0f),
        "visual_score" to (0.0f to 1.0f),
    )

    /** Envelope operasional empiris (p01..p99) dari data sensor IoT nyata. */
    private val ENVELOPE: Map<String, Pair<Float, Float>> = mapOf(
        "ph_sensor" to (5.56f to 11.24f),
        "water_temp" to (22.0f to 26.69f),
        "tds_ppm" to (268.0f to 1028.0f),
    )

    private val RECOMMENDATIONS: Map<String, Recommendation> = mapOf(
        "INSUFFICIENT_EVIDENCE" to Recommendation("RISK_000", "Jangan mengambil kesimpulan. Ulangi pengukuran: periksa koneksi sensor, bersihkan probe, dan ambil ulang foto sesuai panduan pencahayaan RonaCard.", 1, "Tidak ada.", 15, "NEEDS_EXPERT_VALIDATION"),
        "NORMAL" to Recommendation("RISK_001", "Lanjutkan pemantauan rutin sesuai jadwal SOP tambak.", 4, "Tidak ada.", 360, "NEEDS_EXPERT_VALIDATION"),
        "WASPADA" to Recommendation("RISK_002", "Periksa ulang kualitas air dan evaluasi pemberian pakan sesuai SOP. Perpendek interval pemantauan.", 3, "Jangan mengubah kimia air hanya berdasarkan satu pembacaan.", 120, "NEEDS_EXPERT_VALIDATION"),
        "SIAGA" to Recommendation("RISK_003", "Verifikasi pembacaan dengan alat ukur pembanding, siapkan aerasi tambahan, dan tinjau tindakan korektif sesuai SOP tambak.", 2, "Hindari pergantian air besar mendadak tanpa mengukur suhu dan pH air pengganti.", 30, "NEEDS_EXPERT_VALIDATION"),
        "DARURAT" to Recommendation("RISK_004", "Tangani segera sesuai SOP kedaruratan tambak dan hubungi penyuluh/teknisi. Verifikasi ulang pembacaan sambil menyiapkan aerasi darurat.", 1, "Jangan menunda tindakan hanya untuk menunggu konfirmasi laboratorium.", 10, "NEEDS_EXPERT_VALIDATION"),
    )

    private val TRIGGER_RECOMMENDATIONS = listOf(
        TriggerRecommendation("TRG_DO_LOW", setOf("R001", "R002", "R003"), "Bukti menunjuk oksigen terlarut rendah (ESTIMATED DO, bukan hasil ukur langsung). Konfirmasi dengan DO meter bila ada, dan periksa aerator.", "Estimasi DO belum tervalidasi terhadap DO meter; jangan dipakai sebagai satu-satunya dasar keputusan besar.", "NEEDS_EXPERT_VALIDATION"),
        TriggerRecommendation("TRG_PH_EXTREME", setOf("R010", "R011", "R012", "R013"), "Bukti menunjuk pH di luar kisaran umum. Kalibrasi ulang probe pH sebelum melakukan tindakan kimia apa pun.", "Jangan menambahkan kapur/asam tanpa SOP dan pengukuran ulang.", "NEEDS_EXPERT_VALIDATION"),
        TriggerRecommendation("TRG_TEMP", setOf("R020", "R021"), "Suhu air di luar kisaran nyaman. Tinjau naungan, kedalaman, dan jadwal pakan.", "Tidak ada.", "NEEDS_EXPERT_VALIDATION"),
        TriggerRecommendation("TRG_VISUAL", setOf("R030", "R031"), "Rona air berubah. Ini indikator VISUAL saja — tidak menunjukkan spesies alga maupun konsentrasi toksin. Amati ulang pada pencahayaan yang sama.", "Dilarang menyimpulkan HAB atau toksisitas dari foto.", "NEEDS_EXPERT_VALIDATION"),
        TriggerRecommendation("TRG_PH_DISAGREE", setOf("R040"), "pH dari foto dan pH sensor tidak sepakat. Ini sinyal KUALITAS DATA: kalibrasi ulang probe dan foto ulang strip pada pencahayaan yang benar.", "Jangan menganggap salah satu nilai otomatis benar.", "NEEDS_EXPERT_VALIDATION"),
    )

    private val REQUIRED = listOf("water_temp", "ph_sensor")
    private val HIGH_VALUE = listOf("do_est", "visual_score")

    fun assessRisk(input: RiskInput): RiskOutput {
        val values = HashMap<String, Float?>()
        values["water_temp"] = input.waterTemp
        values["ph_sensor"] = input.phSensor
        values["do_est"] = input.doEst
        values["ec_value"] = input.ecValue
        values["tds_ppm"] = input.tdsPpm
        values["visual_score"] = input.visualScore
        if (input.phVisualEst != null && input.phSensor != null) {
            values["ph_difference"] = kotlin.math.abs(input.phVisualEst - input.phSensor)
        }

        val warnings = ArrayList<String>()

        // 1) plausibility / sensor fault
        for ((param, range) in PLAUSIBLE) {
            val v = values[param] ?: continue
            if (v < range.first || v > range.second) {
                warnings.add("$param=$v di luar rentang fisik [${range.first}, ${range.second}] " +
                             "-> kemungkinan sensor fault")
                values[param] = null
            }
        }

        // 2) out-of-distribution
        val ood = ArrayList<String>()
        for ((param, range) in ENVELOPE) {
            val v = values[param] ?: continue
            if (v < range.first || v > range.second) {
                ood.add("$param=$v di luar envelope operasional [${range.first}, ${range.second}]")
            }
        }

        // 3) kualitas data
        val missingRequired = REQUIRED.filter { values[it] == null }
        val missingHighValue = HIGH_VALUE.filter { values[it] == null }
        val imageFailed = input.imageQuality?.uppercase() in listOf("FAIL", "BAD", "POOR")
        if (imageFailed) values["visual_score"] = null

        if (missingRequired.isNotEmpty() || missingHighValue.size == HIGH_VALUE.size) {
            val why = ArrayList<String>()
            if (missingRequired.isNotEmpty()) why.add("parameter wajib tidak tersedia: $missingRequired")
            if (missingHighValue.size == HIGH_VALUE.size) why.add("tidak ada bukti oksigen maupun visual")
            why.addAll(warnings)
            val rec = RECOMMENDATIONS["INSUFFICIENT_EVIDENCE"]
            return RiskOutput(
                status = "INSUFFICIENT_EVIDENCE", factors = why,
                recommendations = listOfNotNull(rec?.action),
                dataQuality = DataQuality.INSUFFICIENT_EVIDENCE.name,
                contraindications = listOfNotNull(rec?.contraindication),
                recheckAfterMinutes = rec?.recheckAfterMinutes
            )
        }

        val quality = when {
            warnings.isNotEmpty() || imageFailed || missingHighValue.isNotEmpty() -> DataQuality.DEGRADED
            ood.isNotEmpty() -> DataQuality.POSSIBLE_OUT_OF_DISTRIBUTION
            else -> DataQuality.OK
        }

        // 4) evaluasi rule, ambil severity tertinggi
        var worst = RiskLevel.NORMAL
        val factors = ArrayList<String>()
        val fired = ArrayList<String>()
        for (r in RULES) {
            val v = values[r.parameter] ?: continue
            val hit = when (r.op) {
                "<" -> v < r.threshold
                "<=" -> v <= r.threshold
                ">" -> v > r.threshold
                ">=" -> v >= r.threshold
                "==" -> v == r.threshold
                else -> false
            }
            if (hit) {
                fired.add(r.id)
                factors.add("[${r.id}] ${r.parameter}=$v ${r.op} ${r.threshold} ${r.unit} " +
                            "-> ${r.level.name} (${r.validationStatus})")
                if (r.level.severity > worst.severity) worst = r.level
            }
        }
        if (factors.isEmpty()) factors.add("Semua parameter yang tersedia berada dalam rentang rule PROVISIONAL.")
        factors.addAll(warnings)
        factors.addAll(ood)

        // 5) rekomendasi
        val actions = ArrayList<String>()
        val contras = ArrayList<String>()
        var recheck: Int? = null
        RECOMMENDATIONS[worst.name]?.let {
            actions.add(it.action); contras.add(it.contraindication); recheck = it.recheckAfterMinutes
        }
        for (t in TRIGGER_RECOMMENDATIONS) {
            if (t.triggerRules.any { it in fired }) {
                actions.add(t.action); contras.add(t.contraindication)
            }
        }

        return RiskOutput(
            status = worst.name, factors = factors, recommendations = actions,
            dataQuality = quality.name, firedRules = fired,
            contraindications = contras.filter { it != "Tidak ada." },
            recheckAfterMinutes = recheck
        )
    }
}
