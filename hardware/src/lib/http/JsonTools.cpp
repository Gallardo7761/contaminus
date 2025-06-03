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
#ifdef JSON_PRINTS
  Serial.println("📜 JSON generado:"); 
  Serial.print("\t");
  Serial.println(output);
#endif
  return output;
}

#if DEVICE_ROLE == ACTUATOR
MAX7219Status_t deserializeActuatorStatus(HTTPClient &http, int httpResponseCode)
{
  if (httpResponseCode > 0)
  {
#ifdef JSON_PRINTS
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
#endif

    String responseJson = http.getString();
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
#ifdef JSON_PRINTS
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
#endif
      return {
          .status = "error",
          .actuatorStatus = "Error"};
    }

    String status = doc["status"] | "error";
    String actuatorStatus = doc["actuatorStatus"] | "Unknown";

#ifdef JSON_PRINTS
    Serial.println("Actuator status deserialized:");
    Serial.printf("  Status: %s\n  Actuator Status: %s\n\n", status.c_str(), actuatorStatus.c_str());
#endif

    return {
        .status = status,
        .actuatorStatus = actuatorStatus};
  }
  else
  {
#ifdef JSON_PRINTS
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
#endif
    return {
        .status = "error",
        .actuatorStatus = "HTTP error"};
  }
}
#endif

int deserializeGroupId(HTTPClient &http, int httpResponseCode)
{
  if (httpResponseCode > 0)
  {
#ifdef JSON_PRINTS
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
#endif
    String responseJson = http.getString();
#ifdef JSON_PRINTS
    Serial.println("Response JSON: " + responseJson);
#endif
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
#ifdef JSON_PRINTS
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
#endif
      return -1;
    }

    if(doc["message"].isNull())
      return -1;

    return doc["message"].as<int>();
  }
  return -1;
}