#include "BME280.hpp"

BME280I2C bme;

void BME280_Init()
{
    Wire.setPins(21, 22);
    Wire.begin();
    while(!bme.begin())
    {
        Serial.println("Could not find BME280 sensor!");
        delay(1000);
    }
}

bool BME280_Read(float &pressure, float &temperature, float &humidity)
{
    BME280::TempUnit tempUnit(BME280::TempUnit_Celsius);
    BME280::PresUnit presUnit(BME280::PresUnit_Pa);
    bme.read(pressure, temperature, humidity, tempUnit, presUnit);

    return (temperature != 0.0f && pressure != 0.0f);
}

