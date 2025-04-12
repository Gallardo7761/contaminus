#include <Arduino.h>

#include "JsonTools.hpp"
#include "RestClient.hpp"
#include "WifiConnection.hpp"
#include "MqttClient.hpp"
#include "MQ7.hpp"
#include "BMP280.hpp"
//#include "test.hpp"


#define LED 2
#define SERVER_IP "192.168.1.178"
#define REST_PORT 80
#define MQTT_PORT 1883
