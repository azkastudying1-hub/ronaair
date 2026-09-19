#ifndef RONAIR_ESP32_MODEL_H
#define RONAIR_ESP32_MODEL_H
#include <math.h>

// RonaAir DO soft sensor. Output is Estimated DO (mg/L), not measured DO.
// Locked model: Model C

struct DOEstimateResult {
  float estimated_do_mg_l;
  int model_id;
  bool in_training_range;
  bool ph_used;
};

const float MODEL_A_B0 = 12.83670706f;
const float MODEL_A_B_WTEMP = -0.2574012866f;

const float MODEL_B_B0 = 12.68255796f;
const float MODEL_B_B_WTEMP = -0.25824716f;
const float MODEL_B_B_EC = 0.003472488643f;

const float MODEL_C_B0 = 5.607391237f;
const float MODEL_C_B_WTEMP = -0.2633041905f;
const float MODEL_C_B_EC = -0.02183120849f;
const float MODEL_C_B_PH = 1.158251551f;

const float TEMP_MIN = 0.01f;
const float TEMP_MAX = 22.04f;
const float TDS_OR_EC_MIN = 27.7f;
const float TDS_OR_EC_MAX = 96.9f;
const float PH_MIN = 6.57f;
const float PH_MAX = 7.54f;

inline bool insideRange(float value, float low, float high) {
  if (isnan(low) || isnan(high)) return true;
  return value >= low && value <= high;
}

inline float estimateDO_MODEL_A(float wtemp, float tds_or_ec, float ph) {
  return MODEL_A_B0 + (MODEL_A_B_WTEMP * wtemp);
}

inline float estimateDO_MODEL_B(float wtemp, float tds_or_ec, float ph) {
  return MODEL_B_B0 + (MODEL_B_B_WTEMP * wtemp) + (MODEL_B_B_EC * tds_or_ec);
}

inline float estimateDO_MODEL_C(float wtemp, float tds_or_ec, float ph) {
  return MODEL_C_B0 + (MODEL_C_B_WTEMP * wtemp) + (MODEL_C_B_EC * tds_or_ec) + (MODEL_C_B_PH * ph);
}

inline DOEstimateResult estimateDOWithStatus(float temperature, float tds_or_ec, float ph, bool ph_available) {
  DOEstimateResult result;
  result.in_training_range = insideRange(temperature, TEMP_MIN, TEMP_MAX);
  result.ph_used = false;
  if (ph_available && !isnan(ph)) {
    result.estimated_do_mg_l = estimateDO_MODEL_C(temperature, tds_or_ec, ph);
    result.model_id = 3;
    result.ph_used = true;
    result.in_training_range = result.in_training_range && insideRange(tds_or_ec, TDS_OR_EC_MIN, TDS_OR_EC_MAX) && insideRange(ph, PH_MIN, PH_MAX);
    return result;
  }
  result.estimated_do_mg_l = estimateDO_MODEL_B(temperature, tds_or_ec, ph);
  result.model_id = 2;
  result.in_training_range = result.in_training_range && insideRange(tds_or_ec, TDS_OR_EC_MIN, TDS_OR_EC_MAX);
  return result;
}

inline float estimateDO(float temperature, float tds_or_ec, float ph) {
  return estimateDOWithStatus(temperature, tds_or_ec, ph, !isnan(ph)).estimated_do_mg_l;
}

#endif