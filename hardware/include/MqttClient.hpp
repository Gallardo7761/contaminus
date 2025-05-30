#pragma once

#include "globals.hpp"
#include <WiFi.h>
#include <PubSubClient.h>
#include "RestClient.hpp"

#define USER "contaminus"
#define MQTT_PASSWORD "contaminus"

void MQTT_OnReceived(char *topic, byte *payload, unsigned int length);
void MQTT_Init(const char *MQTTServerAddress, uint16_t MQTTServerPort);
void MQTT_Connect(const char *MQTTClientName);
void MQTT_Handle(const char *MQTTClientName);
String buildTopic(int groupId, const String& deviceId, const String& topic);