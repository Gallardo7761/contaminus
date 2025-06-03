#include <Arduino.h>

#define DEVICE_ROLE SENSOR // se cambia entre SENSOR y ACTUATOR

#define MQTT_URI "miarma.net"
#define API_URI "https://contaminus.miarma.net/api/v1/"
#define RAW_API_URI "https://contaminus.miarma.net/api/raw/v1/"
#define REST_PORT 443
#define MQTT_PORT 1883

#define MQ7_ID 3
#define BME280_ID 2
#define GPS_ID 1
#define MAX7219_ID 1

#define SENSOR 0
#define ACTUATOR 1

#define DEBUG

extern const uint32_t DEVICE_ID;
extern int GROUP_ID;
extern String currentMessage;