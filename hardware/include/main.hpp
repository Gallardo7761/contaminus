#include <Arduino.h>

#define LED 2
#define SERVER_IP "192.168.1.178"
#define REST_PORT 80
#define MQTT_PORT 1883

#include "JsonTools.hpp"
#include "RestClient.hpp"
#include "WifiConnection.hpp"
#include "MqttClient.hpp"
#include "BME280.hpp"

uint32_t getChipID();