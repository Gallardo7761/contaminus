#pragma once

#include "globals.hpp"

#if DEVICE_ROLE == SENSOR
  #warning "Compilando firmware para SENSOR"
#elif DEVICE_ROLE == ACTUATOR
  #warning "Compilando firmware para ACTUATOR"
#else
  #warning "DEVICE_ROLE no definido correctamente"
#endif

#include "JsonTools.hpp"
#include "RestClient.hpp"
#include "WifiConnection.hpp"
#include "MqttClient.hpp"
#if DEVICE_ROLE == SENSOR
#include "BME280.hpp"
#include "GPS.hpp"
#include "MQ7v2.hpp"
#endif
#if DEVICE_ROLE == ACTUATOR
#include "MAX7219.hpp"
#endif

struct TaskTimer
{
    uint32_t lastRun = 0;
    uint32_t interval = 1000;

    TaskTimer() = default;

    TaskTimer(uint32_t last, uint32_t interval)
        : lastRun(last), interval(interval) {}
};

struct SensorInfo
{
    int id;
    String type;
};

void readMQ7();
void readBME280();
void readGPS();
void writeMatrix(const char *message);
void printAllData();
void sendSensorData();
uint32_t getChipID();
int getGroupId(int deviceId);