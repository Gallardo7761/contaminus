#include "main.hpp"

const uint32_t DEVICE_ID = getChipID();
const char ALL_VEHICLES[] = "Todo tipo de vehiculos";
const char ELECTRIC_VEHICLES[] = "Solo vehiculos electricos/hibridos";
const char *currentMessage = nullptr;

TaskTimer matrixTimer{0, 25};
TaskTimer globalTimer{0, 60000};

extern HTTPClient httpClient;
extern MD_Parola display;

MQ7Data_t mq7Data;
BME280Data_t bme280Data;
GPSData_t gpsData;
AirQualityStatus currentAirStatus = GOOD;

void setup()
{
    Serial.begin(115200);

    Serial.println("Iniciando...");

    setupWifi();

    BME280_Init();
    Serial.println("Sensor BME280 inicializado");
    GPS_Init();
    Serial.println("Sensor GPS inicializado");
    MQ7_Init();
    Serial.println("Sensor MQ7 inicializado");
    MAX7219_Init();
    Serial.println("Display inicializado");

    writeMatrix(ALL_VEHICLES);
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

        globalTimer.lastRun = now;
    }
}

void readMQ7()
{
    const float CO_THRESHOLD = 100.0f;

    mq7Data = MQ7_Read();

    AirQualityStatus newStatus = (mq7Data.co >= CO_THRESHOLD) ? BAD : GOOD;

    if (newStatus != currentAirStatus)
    {
        currentAirStatus = newStatus;
        if (currentAirStatus == BAD)
        {
            writeMatrix(ELECTRIC_VEHICLES);
        }
        else
        {
            writeMatrix(ALL_VEHICLES);
        }
    }
}

void readBME280()
{
    bme280Data = BME280_Read();
}

void readGPS()
{
    gpsData = GPS_Read();
}

void writeMatrix(const char *message)
{
    if (currentMessage == message)
        return;
    currentMessage = message;

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
    Serial.print(bme280Data.pressure / 100);
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
