#include <Arduino.h>

#define SERVER_IP "https://contaminus.miarma.net/api/v1/"
#define REST_PORT 443
#define MQTT_PORT 1883

#define GROUP_ID 1

#define MQ7_ID 1
#define BME280_ID 2
#define GPS_ID 3
#define MAX7219_ID 1

#define DEBUG

#include "JsonTools.hpp"
#include "RestClient.hpp"
#include "WifiConnection.hpp"
#include "MqttClient.hpp"
#include "BME280.hpp"
#include "GPS.hpp"
#include "MAX7219.hpp"
#include "MQ7v2.hpp"

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

enum AirQualityStatus
{
    GOOD,
    BAD
};

void readMQ7();
void readBME280();
void readGPS();
void writeMatrix(const char *message);
void printAllData();
void sendSensorData();
uint32_t getChipID();