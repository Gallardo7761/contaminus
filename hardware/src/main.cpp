#include "main.hpp"

#define DEBUG

const uint32_t DEVICE_ID = getChipID();
const char ALL_VEHICLES[] = "Todo tipo de vehiculos";
const char ELECTRIC_VEHICLES[] = "Solo vehiculos electricos/hibridos";

TaskTimer bmeTimer{0, 1000};
TaskTimer mq7Timer{0, 1000};
TaskTimer gpsTimer{0, 1000};
TaskTimer printTimer{0, 1000};
TaskTimer matrixTimer{0, 25};

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
    BME280_Init();
    Serial.println("Sensor BME280 inicializado");
    MAX7219_Init();
    Serial.println("Display inicializado");
    MQ7_Init();
    Serial.println("Sensor MQ7 inicializado"); 

    writeMatrix(ALL_VEHICLES);
}

void loop()
{
    uint32_t now = millis();

    if (now - matrixTimer.lastRun >= matrixTimer.interval) {
        if (MAX7219_Animate()) {
            MAX7219_ResetAnimation();
        }
        matrixTimer.lastRun = now;
    }
    
    if (now - bmeTimer.lastRun >= bmeTimer.interval) {
        readBME280();
        bmeTimer.lastRun = now;
    }

    if (now - mq7Timer.lastRun >= mq7Timer.interval) {
        readMQ7();
        mq7Timer.lastRun = now;
    }

    if (now - gpsTimer.lastRun >= gpsTimer.interval) {
        readGPS();
        gpsTimer.lastRun = now;
    }

    #ifdef DEBUG
    if (now - printTimer.lastRun >= printTimer.interval) {
        Serial.print("---------------------\n");
        Serial.println("Leyendo sensores...");
        printTimer.lastRun = now;
    }
    #endif
}

void readMQ7()
{
    mq7Data = MQ7_Read();

    #ifdef DEBUG
    Serial.print("CO: "); Serial.println(mq7Data.co);
    #endif

    const float CO_THRESHOLD = 10.0f;

    AirQualityStatus newStatus = (mq7Data.co >= CO_THRESHOLD) ? BAD : GOOD;

    if (newStatus != currentAirStatus) {
        currentAirStatus = newStatus;
        if (currentAirStatus == BAD) {
            writeMatrix(ELECTRIC_VEHICLES);
        } else {
            writeMatrix(ALL_VEHICLES);
        }
    }
}


void readBME280()
{
    bme280Data = BME280_Read();
    #ifdef DEBUG
    Serial.print("Presión: "); Serial.print(bme280Data.pressure / 100); Serial.println(" hPa");
    Serial.print("Temperatura: "); Serial.print(bme280Data.temperature); Serial.println(" °C");
    Serial.print("Humedad: "); Serial.print(bme280Data.humidity); Serial.println(" %");
    #endif
}

void readGPS()
{
    gpsData = GPS_Read();
    #ifdef DEBUG
    Serial.print("Latitud: "); Serial.println(gpsData.lat);
    Serial.print("Longitud: "); Serial.println(gpsData.lon);
    #endif
}

void writeMatrix(const char* message)
{
    #ifdef DEBUG
    Serial.println("Escribiendo en el display...");
    #endif
    MAX7219_DisplayText(message, PA_LEFT, 50, 0);
}

uint32_t getChipID()
{
    uint32_t chipId = 0;
    for (int i = 0; i < 17; i += 8)
    {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
    #ifdef DEBUG
    Serial.print("Chip ID: "); Serial.println(chipId, HEX);
    #endif
    return chipId;
}
