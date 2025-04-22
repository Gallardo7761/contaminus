#include "main.hpp"

const uint32_t deviceId = getChipID();

// instances
HTTPClient httpClient;

// HTTP Request
String response;

// MQ7
float sensorVolt, sensorValue, RSAir, R0;

// BMP280
float temperature, pressure, altitude;

uint32_t getChipID()
{
    uint32_t chipId;
    for (int i = 0; i < 17; i = i + 8)
    {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
    return chipId;
}

void setup()
{
    Serial.begin(9600);

    /*// WiFi Connection
    if(setupWifi() != 0)
    {
        Serial.print("Error connecting to WiFi");
    }

    // test get
    getRequest(httpClient, "http://172.20.10.7:8082/api/v1/sensors/1/values", response);
    deserializeSensorValue(httpClient, httpClient.GET()); */

    BMP280_Init();
}

void loop()
{
    if (BMP280_Read(temperature, pressure, altitude))
    {
        Serial.print("Temperature: ");
        Serial.println(temperature);
        Serial.print("Pressure: ");
        Serial.println(pressure);
        Serial.print("Altitude: ");
        Serial.println(altitude);
    }
    else
    {
        Serial.println("❌ Lectura fallida del BMP280");
    }

    delay(2000);
}