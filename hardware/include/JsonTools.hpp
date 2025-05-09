#pragma once

#include <ArduinoJson.h>
#include <HTTPClient.h>
#include "BME280.hpp"
#include "MQ7v2.hpp"
#include "GPS.hpp"

String serializeSensorValue(
    int sensorId,
    const String &deviceId,
    const String &sensorType,
    const String &unit,
    int sensorStatus,
    const BME280Data_t &bme,
    const MQ7Data_t &mq7,
    const GPSData_t &gps,
    long timestamp);

String serializeActuatorStatus(
    int actuatorId,
    const String &deviceId,
    int status,
    long timestamp);

void deserializeSensorValue(HTTPClient &http, int httpResponseCode);
void deserializeActuatorStatus(HTTPClient &http, int httpResponseCode);
void deserializeDevice(HTTPClient &http, int httpResponseCode);