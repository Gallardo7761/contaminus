#include "BME280.hpp"

BME280I2C bme;

void BME280_Init()
{
    Wire.setPins(21, 22);
    Wire.begin();

    BME280I2C::Settings settings(
        BME280I2C::OSR::OSR_X1, 
        BME280I2C::OSR::OSR_X1, 
        BME280I2C::OSR::OSR_X1,
        BME280I2C::Mode::Mode_Forced,  // modo forzado
        BME280I2C::StandbyTime::StandbyTime_1000ms,
        BME280I2C::Filter::Filter_16,
        BME280I2C::SpiEnable::SpiEnable_False,
        BME280I2C::I2CAddr::I2CAddr_0x76  // dirección I2C del BME280
    );

    bme.setSettings(settings);

    while (!bme.begin());
}

BME280Data_t BME280_Read()
{
    float p, t, h;
    BME280::TempUnit tUnit(BME280::TempUnit_Celsius);
    BME280::PresUnit pUnit(BME280::PresUnit_Pa);
    bme.read(p, t, h, tUnit, pUnit);
    return {p, t, h};
}

