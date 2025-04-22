#include <Wire.h>
#include <BMP280_DEV.h>

#define I2C_BMP280_ADDRESS 0x76

void BMP280_Init();
bool BMP280_Read(float &temperature, float &pressure, float &altitude);