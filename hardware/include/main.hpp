#include <Arduino.h>

#define SERVER_IP "https://contaminus.miarma.net/api/v1/"
#define REST_PORT 443
#define MQTT_PORT 1883

#include "JsonTools.hpp"
#include "RestClient.hpp"
#include "WifiConnection.hpp"
#include "MqttClient.hpp"
#include "BME280.hpp"
#include "GPS.hpp"
#include "MAX7219.hpp"
#include "MQ7.hpp"

uint32_t getChipID();
void prettyReadMQ7();
void prettyReadBME280();
void prettyReadGPS();
void testMatrix();