#include <Arduino.h>

#define MQ7_A0 34
#define MQ7_D0 35
#define R0 20000
#define RL 10000

struct MQ7Data_t
{
    float co;
    bool threshold;
};

void MQ7_Init();
MQ7Data_t MQ7_Read();