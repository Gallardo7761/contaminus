#pragma once

#include <ArduinoJson.h>
#include <HTTPClient.h>
#if DEVICE_ROLE == SENSOR
#include "BME280.hpp"
#include "MQ7v2.hpp"
#include "GPS.hpp"
#endif
#if DEVICE_ROLE == ACTUATOR
#include "MAX7219.hpp"
#endif

#if DEVICE_ROLE == SENSOR
String serializeSensorValue(
    int groupId,
    const String &deviceId,
    int gpsSensorId,
    int weatherSensorId,
    int coSensorId,
    const BME280Data_t &bme,
    const MQ7Data_t &mq7,
    const GPSData_t &gps);
#endif

#if DEVICE_ROLE == ACTUATOR
MAX7219Status_t deserializeActuatorStatus(HTTPClient &http, int httpResponseCode);
#endif
int deserializeGroupId(HTTPClient &http, int httpResponseCode);