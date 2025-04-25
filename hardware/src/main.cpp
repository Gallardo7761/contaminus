#include "main.hpp"

const uint32_t deviceId = getChipID();

extern HTTPClient httpClient; // HTTP client object
String response; // HTTP Response
float sensorVolt, sensorValue, RSAir, R0; // MQ7 vars
float temperature, pressure, humidity; // BME280 vars
float lon, lat; // GPS vars
extern MD_Parola display; // Display object

void setup()
{
    Serial.begin(9600);

    Serial.println("Iniciando...");
    MQ7_Init();
    Serial.println("Sensor MQ7 inicializado");
    BME280_Init();
    Serial.println("Sensor BME280 inicializado");
    GPS_Init();
    Serial.println("GPS inicializado");
    MAX7219_Init();
    Serial.println("Display inicializado");

    prettyReadBME280();
    prettyReadMQ7();
    testMatrix();
}

void loop()
{
    
}

void prettyReadMQ7()
{
    Serial.println("Leyendo sensor MQ7...");
    MQ7_Read(sensorVolt, RSAir, R0, sensorValue);
    Serial.print("\t - Voltaje: "); Serial.print(sensorVolt); Serial.print("V\r\n");
    Serial.print("\t - Valor sensor: "); Serial.print(sensorValue); Serial.print("\r\n");
    Serial.print("\t - Resistencia aire: "); Serial.print(RSAir); Serial.print("kOhm\r\n");
    Serial.print("\t - Resistencia aire: "); Serial.print(R0); Serial.print("kOhm\r\n");
    Serial.print("\t - Concentración CO: "); Serial.print(sensorValue); Serial.print("ppm\r\n");
}

void prettyReadBME280()
{
    Serial.println("Leyendo sensor BME280...");
    BME280_Read(pressure, temperature, humidity);
    Serial.print("\t - Presión: "); Serial.print(pressure/100); Serial.print("hPa\r\n");
    Serial.print("\t - Temperatura: "); Serial.print(temperature); Serial.print("°C\r\n");
    Serial.print("\t - Humedad: "); Serial.print(humidity); Serial.print("%\r\n");
}

void prettyReadGPS()
{
    Serial.println("Leyendo GPS...");
    GPS_Read(lat, lon);
    Serial.print("\t - Latitud: "); Serial.print(lat); Serial.print("\r\n");
    Serial.print("\t - Longitud: "); Serial.print(lon); Serial.print("\r\n");
}

void testMatrix()
{
    Serial.println("Escribiendo en el display...");
    MAX7219_DisplayText("Prueba de texto", PA_LEFT, 100, 500);
}

uint32_t getChipID()
{
    uint32_t chipId;
    for (int i = 0; i < 17; i = i + 8)
    {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
    return chipId;
}