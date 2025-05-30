#include "MQ7v2.hpp"

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

    if (random(0, 100) < 50) {
        ppm = random(80, 500);  // valores entre 101 y 500 ppm
        d0 = true;
    } else {
        ppm = random(10, 79);   // valores entre 10 y 99 ppm
        d0 = false;
    }

    return {ppm, d0};
}
