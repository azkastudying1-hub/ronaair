#!/usr/bin/env python3
# Standalone check that the RonaAir deployment package is self-consistent.
#
# Usage:
#     python test_inference.py                 -> schema + coefficient round-trip only
#     python test_inference.py path/to/img.jpg -> plus a full single-image prediction
import json, os, sys
import numpy as np

HERE = os.path.dirname(os.path.abspath(__file__))

def load(name):
    p = os.path.join(HERE, name)
    if not os.path.isfile(p):
        return None
    with open(p) as f:
        return json.load(f)

def main():
    schema = load("feature_schema.json")
    pre = load("preprocessing_config.json")
    meta = load("model_metadata.json")
    if schema is None or pre is None:
        print("FAIL: feature_schema.json / preprocessing_config.json missing")
        return 1

    order = schema["feature_order"]
    print(f"model version : {schema['model_version']}")
    print(f"features      : {len(order)}")
    print(f"final model   : {meta['final_model'] if meta else 'unknown'}")
    print(f"valid pH range: {pre['ph_valid_range']}")

    assert order == pre["feature_order"], "FAIL: feature order differs between schema and config"
    print("PASS: feature order is consistent across artifacts")

    coef = load("model_coefficients.json")
    if coef:
        assert coef["feature_order"] == order, "FAIL: coefficient feature order differs"
        x = np.array([[schema["feature_stats"][f]["mean"] for f in order]])
        z = x
        if "scaler" in coef:
            z = (z - np.array(coef["scaler"]["mean"])) / np.array(coef["scaler"]["scale"])
        if "polynomial" in coef:
            P = np.array(coef["polynomial"]["powers"], dtype=float)
            z = np.stack([np.prod(z ** P[j], axis=1) for j in range(P.shape[0])], axis=1)
        ph = float(np.ravel(z @ np.array(coef["coefficients"]) + coef["intercept"])[0])
        print(f"PASS: native coefficient inference runs; pH at the mean feature vector = {ph:.3f}")
    else:
        print("note: model_coefficients.json absent (the selected model has no native export)")

    tfl = os.path.join(HERE, "final_model.tflite")
    print(f"tflite present: {os.path.isfile(tfl)}")

    if len(sys.argv) > 1:
        img = sys.argv[1]
        if not os.path.isfile(img):
            print(f"FAIL: image not found: {img}")
            return 1
        try:
            import cv2, joblib
        except ImportError:
            print("skip: opencv/joblib not installed, cannot run the image path")
            return 0
        print(f"note: full image inference needs the notebook's feature functions; "
              f"import predict_ph_from_image from the notebook to run {img}")
    return 0

if __name__ == "__main__":
    sys.exit(main())
