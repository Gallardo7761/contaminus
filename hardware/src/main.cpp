#include "main.hpp"

const uint32_t DEVICE_ID = getChipID();
const int GROUP_ID = 1;
const char *currentMessage = nullptr;
const String id = "CUS-" + String(DEVICE_ID, HEX);

TaskTimer matrixTimer{0, 25};
TaskTimer globalTimer{0, 60000};
TaskTimer mqttTimer{0, 5000};

extern HTTPClient httpClient;
String response;
extern MD_Parola display;

MQ7Data_t mq7Data;
BME280Data_t bme280Data;
GPSData_t gpsData;

void setup()
{
    Serial.begin(115200);

    Serial.println("Iniciando...");

    setupWifi();
    MQTT_Init(MQTT_URI, MQTT_PORT);

    BME280_Init();
    Serial.println("Sensor BME280 inicializado");
    GPS_Init();
    Serial.println("Sensor GPS inicializado");
    MQ7_Init();
    Serial.println("Sensor MQ7 inicializado");
    MAX7219_Init();
    Serial.println("Display inicializado");

    writeMatrix(currentMessage);
}

void loop()
{
    uint32_t now = millis();

    if (now - matrixTimer.lastRun >= matrixTimer.interval)
    {
        if (MAX7219_Animate())
        {
            MAX7219_ResetAnimation();
        }
        matrixTimer.lastRun = now;
    }

    if (now - globalTimer.lastRun >= globalTimer.interval)
    {
        readBME280();
        readGPS();
        readMQ7();

#ifdef DEBUG
        printAllData();
#endif

        sendSensorData();

        globalTimer.lastRun = now;
    }

    if (now - mqttTimer.lastRun >= mqttTimer.interval)
    {
        MQTT_Handle(id.c_str());
        mqttTimer.lastRun = now;
    }
}

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

void writeMatrix(const char *message)
{
#ifdef DEBUG
    Serial.println("Escribiendo en el display...");
#endif

    MAX7219_DisplayText(message, PA_LEFT, 50, 0);
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

    // Validaciones básicas (puedes añadir más si quieres)
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
    Serial.println("📬 Respuesta del servidor:");
    Serial.println(response);
#endif
}

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
