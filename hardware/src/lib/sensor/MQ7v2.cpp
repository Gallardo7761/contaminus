#include "MQ7v2.hpp"

uint8_t flag = 0;

void MQ7_Init()
{
    pinMode(MQ7_A0, INPUT);
    pinMode(MQ7_D0, INPUT);
}

MQ7Data_t MQ7_Read()
{
    int16_t raw = analogRead(MQ7_A0);
    float voltage = raw * (5.0 / 4095.0);
    float Rs = (5.0 - voltage) * RL / voltage;
    float ratio = Rs / R0;

    float ppm = pow(10, (-1.5 * log10(ratio) + 0.8));
    bool d0 = digitalRead(MQ7_D0);

    return {ppm, d0};
}

MQ7Data_t MQ7_Read_Fake()
{
    float ppm;
    bool d0;

    if (flag == 0) {
        ppm = 100.0f;  // valores entre 101 y 500 ppm
        d0 = true;
        flag = 1;
    } else {
        ppm = 10.0f;   // valores entre 10 y 99 ppm
        d0 = false;
        flag = 0;
    }

    return {ppm, d0};
}
