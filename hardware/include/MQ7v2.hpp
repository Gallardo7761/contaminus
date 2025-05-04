#include <Arduino.h>

#define MQ7_PIN 34
#define VCC 5.0
#define ADC_RES 1023.0
#define RL 10000.0     // 10kΩ
#define RO 10000.0     // Resistencia del aire limpio

struct MQ7Data_t
{
    float co;
};

void MQ7_Init();
MQ7Data_t MQ7_Read();