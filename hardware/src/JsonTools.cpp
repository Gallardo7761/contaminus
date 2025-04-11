#include "JsonTools.hpp"

String serializeSensorValue(int sensorId, int deviceId, String sensorType, String unit, int sensorStatus,  float temperature, float humidity, float carbonMonoxide, float lat, float lon, long timestamp)
{
  DynamicJsonDocument doc(2048);

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

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  
  return output;
}

String serializeActuatorStatus (int actuatorId, int deviceId, int status, long timestamp)
{
  DynamicJsonDocument doc(2048);

  doc["actuatorId"] = actuatorId;
  doc["deviceId"] = deviceId;
  doc["status"] = status;
  doc["timestamp"] = timestamp;

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  
  return output;
}

String serializeDevice(int sensorId, int deviceId, String sensorType, int status, long timestamp)
{
  DynamicJsonDocument doc(2048);

  doc["sensorId"] = sensorId;
  doc["deviceId"] = deviceId;
  doc["sensorType"] = sensorType;
  doc["status"] = status;
  doc["timestamp"] = timestamp;

  String output;
  serializeJson(doc, output);
  Serial.println(output);
  
  return output;
}

void deserializeSensorValue (HTTPClient &http, int httpResponseCode)
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
      int deviceId = sensor["deviceId"];
      String sensorType = sensor["sensorType"];
      String unit = sensor["unit"];
      int sesnsorStatuts = sensor["sesnsorStatuts"];
      float temperature = sensor["temperature"];
      float humidity = sensor["humidity"];
      float carbonMonoxide = sensor["carbonMonoxide"];
      float lat = sensor["lat"];
      float lon = sensor["lon"];
      long timestamp = sensor["timestamp"];

      Serial.println(("Sensor deserialized: [sensorId: " + String(sensorId) + ", deviceId: " + String(deviceId) + ", sensorType: " + sensorType + ", unit: " + unit +", sesnsorStatuts: " + String(sesnsorStatuts) +", temperature: " + String(temperature) +", humidity: " + String(humidity) +", carbonMonoxide: " + String(carbonMonoxide) +", lat: " + String(lat) +", lon: " + String(lon) +", timestamp: " + String(timestamp) + "]").c_str());
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void deserializeActuatorStatus (HTTPClient &http, int httpResponseCode)
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
      int actuadorId = actuator["actuadorId"];
      int deviceId = actuator["deviceId"];
      int statuts = actuator["statuts"];
      long timestamp = actuator["timestamp"];

      Serial.println(("Actuador deserialized: [actuadorId: " + String(actuadorId) +
      ", deviceId: " + String(deviceId) +
      ", statuts: " + String(statuts) +
      ", timestamp: " + String(timestamp) + "]").c_str());
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void deserializeDevice (HTTPClient &http, int httpResponseCode)
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
      int sensorId = device["sensorId"];
      int deviceId = device["deviceId"];
      String sensorType = device["sensorType"];
      long timestamp = device["timestamp"];

      Serial.println(("Sensor deserialized: [sensorId: " + String(sensorId) +
      ", deviceId: " + String(deviceId) +
      ", sensorType: " + sensorType +
      ", timestamp: " + String(timestamp) + "]").c_str());

    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}