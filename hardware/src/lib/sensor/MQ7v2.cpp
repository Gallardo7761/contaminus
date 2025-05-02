#include "MQ7v2.hpp"

void MQ7_Init()
{
    pinMode(MQ7_PIN, INPUT);
}

void MQ7_Read(float &sensorValue)
{
    int adcValue = analogRead(MQ7_PIN);
    float voltage = (adcValue / ADC_RES) * VCC;

    if (voltage <= 0.1) {
        sensorValue = 0;
        return;
    }

    float rs = (VCC - voltage) * RL / voltage;
    float ratio = rs / RO;
    float ppm = pow(10, (-1.5 * log10(ratio) + 0.8));

    sensorValue = ppm;
}