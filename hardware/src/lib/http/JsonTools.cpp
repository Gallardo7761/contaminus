#include "JsonTools.hpp"

String serializeSensorValue(
    int sensorId,
    const String &deviceId,
    const String &sensorType,
    const String &unit,
    int sensorStatus,
    const BME280Data_t &bme,
    const MQ7Data_t &mq7,
    const GPSData_t &gps,
    long timestamp)
{
  DynamicJsonDocument doc(1024);

  doc["sensorId"] = sensorId;
  doc["deviceId"] = deviceId;
  doc["sensorType"] = sensorType;
  doc["unit"] = unit;
  doc["sensorStatus"] = sensorStatus;
  doc["temperature"] = bme.temperature;
  doc["humidity"] = bme.humidity;
  doc["pressure"] = bme.pressure;
  doc["carbonMonoxide"] = mq7.co;
  doc["lat"] = gps.lat;
  doc["lon"] = gps.lon;
  doc["timestamp"] = timestamp;

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  return output;
}

String serializeActuatorStatus(const int actuatorId, const String &deviceId, const int status, const long timestamp)
{
  DynamicJsonDocument doc(512);

  doc["actuatorId"] = actuatorId;
  doc["deviceId"] = deviceId;
  doc["status"] = status;
  doc["timestamp"] = timestamp;

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  return output;
}

String serializeDevice(const String &deviceId, int groupId, const String &deviceName)
{
  DynamicJsonDocument doc(512);

  doc["deviceId"] = deviceId;
  doc["groupId"] = groupId;
  doc["deviceName"] = deviceName;

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  return output;
}

void deserializeSensorValue(HTTPClient &http, int httpResponseCode)
{
  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    JsonArray array = doc.as<JsonArray>();
    for (JsonObject sensor : array)
    {
      int sensorId = sensor["sensorId"];
      String deviceId = sensor["deviceId"];
      String sensorType = sensor["sensorType"];
      String unit = sensor["unit"];
      int sensorStatus = sensor["sensorStatus"];
      float temperature = sensor["temperature"];
      float humidity = sensor["humidity"];
      float carbonMonoxide = sensor["carbonMonoxide"];
      float lat = sensor["lat"];
      float lon = sensor["lon"];
      long timestamp = sensor["timestamp"];

      Serial.println("Sensor deserialized:");
      Serial.printf("  ID: %d\n  Device: %s\n  Type: %s\n  Unit: %s\n  Status: %d\n  Temp: %.2f\n  Hum: %.2f\n  CO: %.2f\n  Lat: %.6f\n  Lon: %.6f\n  Time: %ld\n\n",
                    sensorId, deviceId.c_str(), sensorType.c_str(), unit.c_str(), sensorStatus,
                    temperature, humidity, carbonMonoxide, lat, lon, timestamp);
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void deserializeActuatorStatus(HTTPClient &http, int httpResponseCode)
{
  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    JsonArray array = doc.as<JsonArray>();
    for (JsonObject actuator : array)
    {
      int actuatorId = actuator["actuatorId"];
      String deviceId = actuator["deviceId"];
      int status = actuator["status"];
      long timestamp = actuator["timestamp"];

      Serial.println("Actuator deserialized:");
      Serial.printf("  ID: %d\n  Device: %s\n  Status: %d\n  Time: %ld\n\n",
                    actuatorId, deviceId.c_str(), status, timestamp);
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void deserializeDevice(HTTPClient &http, int httpResponseCode)
{
  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    JsonArray array = doc.as<JsonArray>();
    for (JsonObject device : array)
    {
      String deviceId = device["deviceId"];
      int groupId = device["groupId"];
      String deviceName = device["deviceName"];

      Serial.println("Device deserialized:");
      Serial.printf("  ID: %s\n  Group: %d\n  Name: %s\n\n", deviceId.c_str(), groupId, deviceName.c_str());
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}