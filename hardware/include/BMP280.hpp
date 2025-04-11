#include <Wire.h>
#include <BMP280_DEV.h>

#define I2C_BMP280_ADDRESS 0x76

void BMP280_Init();
uint8_t BMP280_DataReady();
void BMP280_Read();