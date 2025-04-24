#include "main.hpp"

const uint32_t deviceId = getChipID();

// instances
HTTPClient httpClient;
BMP280_DEV bme;

// HTTP Request
String response;
const String url = "/api/v1/sensors/:sensorId/values";

// MQ7
float sensorVolt, sensorValue, RSAir, R0; 

// BMP280
float temperature, humidity, pressure, altitude;

// GPS
String check;
float lon;
float lat;

uint32_t getChipID()
{
    uint32_t chipId;
    for (int i = 0; i < 17; i = i + 8) {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
    return chipId;
}

void setup() {
    Serial.begin(9600);

    // WiFi Connection
    if(setupWifi() != 0)
    {
        Serial.print("Error connecting to WiFi");
    }

    // test get
    getRequest(httpClient, "http://172.20.10.7:8082/api/v1/sensors/1/values", response);
    deserializeSensorValue(httpClient, httpClient.GET());

    // test gps
    GPS_Init();
    
}
 
void loop() {
    GPS_Read();
    lon = GPS_longitud();
    lat = GPS_latitud();
 //postRequest(HTTPClient &httpClient, const String url, String &payload, String &response)
    postRequest(httpClient, url, serializeSensorValue(), )
}