#pragma once

#include <Wire.h>
#include <BME280I2C.h>

#define I2C_BME280_ADDRESS 0x76

struct BME280Data_t
{
    float pressure;
    float temperature;
    float humidity;
};

void BME280_Init();
BME280Data_t BME280_Read();