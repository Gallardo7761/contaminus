#pragma once

#include <ArduinoJson.h>
#include <HTTPClient.h>
#include "BME280.hpp"
#include "MQ7v2.hpp"
#include "GPS.hpp"
#include "MAX7219.hpp"

String serializeSensorValue(
    int groupId,
    const String &deviceId,
    int gpsSensorId,
    int weatherSensorId,
    int coSensorId,
    const BME280Data_t &bme,
    const MQ7Data_t &mq7,
    const GPSData_t &gps);

MAX7219Status_t deserializeActuatorStatus(HTTPClient &http, int httpResponseCode);
