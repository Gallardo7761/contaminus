#include "main.hpp"

const uint32_t deviceId = getChipID();
String response;
HTTPClient httpClient;

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

    MQ7_init();

    // test get
    getRequest(httpClient, "http://172.20.10.7:8082/api/v1/sensors/1/values", response);
    deserializeSensorValue(httpClient, httpClient.GET());
}
 
void loop() {
    MQ7_read();
}