#include "json.hpp"

String response;

String serializeSensorValue (
    int sensorId,
    int deviceId,
    String sensorType,
    String unit, 
    int sensorStatus,
    float temperature,
    float humidity,
    float carbonMonoxide,
    float lat,
    float lon,
    long timestamp
)
{
    DynamicJsonDocument doc(2048);

    String output;

    doc["sensorId"] = sensorId;
    doc["deviceId"] = deviceId;
    doc["sensorType"] = sensorType;
    doc["unit"] = unit;
    doc["sesnsorStatuts"] = sensorStatus;
    doc["temperature"] = temperature;
    doc["humidity"] = humidity;
    doc["carbonMonoxide"] = carbonMonoxide;
    doc["lat"] = lat;
    doc["lon"] = lon;
    doc["timestamp"] = timestamp;

    serializeJson(doc, output);
    Serial.println(output);
    return output;
} 