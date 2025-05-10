#include "JsonTools.hpp"

String serializeSensorValue(
    int groupId,
    const String &deviceId,
    int gpsSensorId,
    int weatherSensorId,
    int coSensorId,
    const BME280Data_t &bme,
    const MQ7Data_t &mq7,
    const GPSData_t &gps)
{
  DynamicJsonDocument doc(1024);

  doc["groupId"] = groupId;
  doc["deviceId"] = deviceId;

  JsonObject gpsObj = doc.createNestedObject("gps");
  gpsObj["sensorId"] = gpsSensorId;
  gpsObj["lat"] = gps.lat;
  gpsObj["lon"] = gps.lon;

  JsonObject weather = doc.createNestedObject("weather");
  weather["sensorId"] = weatherSensorId;
  weather["temperature"] = bme.temperature;
  weather["humidity"] = bme.humidity;
  weather["pressure"] = bme.pressure;

  JsonObject co = doc.createNestedObject("co");
  co["sensorId"] = coSensorId;
  co["value"] = mq7.co;

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

    String groupId = doc["groupId"];
    String deviceId = doc["deviceId"];

    JsonObject gps = doc["gps"];
    int gpsId = gps["sensorId"];
    float lat = gps["lat"];
    float lon = gps["lon"];

    JsonObject weather = doc["weather"];
    int weatherId = weather["sensorId"];
    float temp = weather["temperature"];
    float hum = weather["humidity"];
    float pres = weather["pressure"];

    JsonObject co = doc["co"];
    int coId = co["sensorId"];
    float coVal = co["value"];

    Serial.println("🛰 GPS:");
    Serial.printf("  Sensor ID: %d\n  Lat: %.6f  Lon: %.6f\n", gpsId, lat, lon);

    Serial.println("🌤 Weather:");
    Serial.printf("  Sensor ID: %d\n  Temp: %.2f°C  Hum: %.2f%%  Pressure: %.2f hPa\n", weatherId, temp, hum, pres);

    Serial.println("🧪 CO:");
    Serial.printf("  Sensor ID: %d\n  CO: %.2f ppm\n", coId, coVal);

    Serial.printf("🧾 Group ID: %s\n", groupId.c_str());
    Serial.printf("🧾 Device ID: %s\n", deviceId.c_str());
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