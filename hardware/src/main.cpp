#include "main.hpp"

const uint32_t DEVICE_ID = getChipID();
const String mqttId = "CUS-" + String(DEVICE_ID, HEX);
const int GROUP_ID = 1;

TaskTimer globalTimer{0, 60000};
TaskTimer mqttTimer{0, 5000};

#if DEVICE_ROLE == ACTUATOR
TaskTimer matrixTimer{0, 25};
const char *currentMessage = ALL;
extern MD_Parola display;
#endif

extern HTTPClient httpClient;
String response;

#if DEVICE_ROLE == SENSOR
MQ7Data_t mq7Data;
BME280Data_t bme280Data;
GPSData_t gpsData;
#endif

void setup()
{
    Serial.begin(115200);

#ifdef DEBUG
    Serial.println("Iniciando...");
#endif

    WiFi_Init();
    MQTT_Init(MQTT_URI, MQTT_PORT);

    try
    {
        
#if DEVICE_ROLE == SENSOR
        BME280_Init();
        Serial.println("Sensor BME280 inicializado");
        GPS_Init();
        Serial.println("Sensor GPS inicializado");
        MQ7_Init();
        Serial.println("Sensor MQ7 inicializado");
#endif

#if DEVICE_ROLE == ACTUATOR
        MAX7219_Init();
        Serial.println("Display inicializado");
        writeMatrix(currentMessage);
#endif
    }
    catch (const char *e)
    {
        Serial.println(e);
    }
}

void loop()
{
    uint32_t now = millis();

#if DEVICE_ROLE == ACTUATOR
    if (now - matrixTimer.lastRun >= matrixTimer.interval)
    {
        if (MAX7219_Animate())
        {
            MAX7219_ResetAnimation();
        }
        matrixTimer.lastRun = now;
    }
#endif

    if (now - globalTimer.lastRun >= globalTimer.interval)
    {
#if DEVICE_ROLE == SENSOR
        readBME280();
        readGPS();
        readMQ7();

#ifdef DEBUG
        printAllData();
#endif

        sendSensorData();
#endif
        globalTimer.lastRun = now;
    }

    if (now - mqttTimer.lastRun >= mqttTimer.interval)
    {
        MQTT_Handle(mqttId.c_str());
        mqttTimer.lastRun = now;
    }
}

#if DEVICE_ROLE == ACTUATOR
void writeMatrix(const char *message)
{
#ifdef DEBUG
    Serial.println("Escribiendo en el display...");
#endif

    MAX7219_DisplayText(message, PA_LEFT, 50, 0);
}
#endif

#if DEVICE_ROLE == SENSOR
void readMQ7()
{
    const float CO_THRESHOLD = 100.0f;
    mq7Data = MQ7_Read();
}

void readBME280()
{
    bme280Data = BME280_Read();
}

void readGPS()
{
    gpsData = GPS_Read_Fake();
}

void printAllData()
{
    Serial.println("---------------------");

    Serial.print("ID: ");
    Serial.println(DEVICE_ID, HEX);

    Serial.print("Presión: ");
    Serial.print(bme280Data.pressure);
    Serial.println(" hPa");
    Serial.print("Temperatura: ");
    Serial.print(bme280Data.temperature);
    Serial.println(" °C");
    Serial.print("Humedad: ");
    Serial.print(bme280Data.humidity);
    Serial.println(" %");

    Serial.print("Latitud: ");
    Serial.println(gpsData.lat);
    Serial.print("Longitud: ");
    Serial.println(gpsData.lon);

    Serial.print("CO: ");
    Serial.println(mq7Data.co);
    Serial.print("D0: ");
    Serial.println(mq7Data.threshold);
}

void sendSensorData()
{
    const String deviceId = String(DEVICE_ID, HEX);

    bool gpsValid = gpsData.lat != 0.0f && gpsData.lon != 0.0f;
    bool weatherValid = bme280Data.temperature != 0.0f &&
                        bme280Data.humidity != 0.0f &&
                        bme280Data.pressure != 0.0f;
    bool coValid = mq7Data.co >= 0.0f;

    if (!gpsValid || !weatherValid || !coValid)
    {
#ifdef DEBUG
        Serial.println("❌ Datos inválidos. No se envía el batch.");
#endif
        return;
    }

    String json = serializeSensorValue(GROUP_ID, deviceId,
                                       GPS_ID, BME280_ID, MQ7_ID,
                                       bme280Data, mq7Data, gpsData);

#ifdef DEBUG
    Serial.println("📤 Enviando datos al servidor...");
#endif

    postRequest(String(API_URI) + "/batch", json, response);

#ifdef DEBUG
    Serial.println("📥 Respuesta del servidor:");
    Serial.println(response);
#endif
}
#endif

uint32_t getChipID()
{
    uint32_t chipId = 0;
    for (int i = 0; i < 17; i += 8)
    {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
#ifdef DEBUG
    Serial.print("Chip ID: ");
    Serial.println(chipId, HEX);
#endif
    return chipId;
}
