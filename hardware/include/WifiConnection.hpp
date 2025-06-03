#pragma once

#include "globals.hpp"
#include <WiFi.h>
#include <PubSubClient.h>

#define SSID "DIGIFIBRA-D2ys"
#define WIFI_PASSWORD "4EEATsyTcZ"

#define PIN_R 12
#define PIN_G 13
#define PIN_B 14

void WiFi_Init();
void WiFi_Handle();
bool WiFi_IsConnected();