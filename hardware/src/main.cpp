#include "main.hpp"

const uint32_t deviceId = getChipID();

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
    if(setup_wifi() != 0)
    {
        Serial.print("Error connecting to WiFI");
    }
}
 
void loop() {
 
}