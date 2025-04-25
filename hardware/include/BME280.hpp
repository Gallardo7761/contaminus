#include <Wire.h>
#include <BME280I2C.h>

#define I2C_BMP280_ADDRESS 0x76

void BME280_Init();
bool BME280_Read(float &pressure, float &temperature, float &humidity);