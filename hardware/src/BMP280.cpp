#include "BMP280.hpp"

BMP280_DEV bme;

void BMP280_Init()
{
    Wire.setPins(21, 22);
    if (!bme.begin(NORMAL_MODE, I2C_BMP280_ADDRESS))
    {
        Serial.println("BMP280 no detectado o error en la inicialización");
    }
    else
    {
        Serial.println("BMP280 inicializado correctamente");
    }
    bme.setTimeStandby(TIME_STANDBY_2000MS);
    bme.startNormalConversion();
}

bool BMP280_Read(float &temperature, float &pressure, float &altitude)
{
    bme.getCurrentMeasurements(temperature, pressure, altitude);
    return (temperature != 0.0f && pressure != 0.0f);
}

