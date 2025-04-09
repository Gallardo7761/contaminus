#include <Arduino.h>

#include "json.hpp"
#include "rest.hpp"
#include "wifi.hpp"
#include "test.hpp"
#include "mqtt.hpp"

#define LED 2
#define SERVER_IP "192.168.1.178"
#define REST_PORT 80
#define MQTT_PORT 1883