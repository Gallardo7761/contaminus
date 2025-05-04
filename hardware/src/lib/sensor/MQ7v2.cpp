#include "MQ7v2.hpp"

void MQ7_Init()
{
    pinMode(MQ7_PIN, INPUT);
}

MQ7Data_t MQ7_Read()
{
    int adcValue = analogRead(MQ7_PIN);
    float voltage = (adcValue / ADC_RES) * VCC;

    if (voltage <= 0.1) {
        return {0.0};
    }

    float rs = (VCC - voltage) * RL / voltage;
    float ratio = rs / RO;
    float ppm = pow(10, (-1.5 * log10(ratio) + 0.8));

    return {ppm};
}