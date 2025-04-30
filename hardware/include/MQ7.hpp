#include <Arduino.h>
 
#define HEATER_PIN 16
#define SENSOR_PIN 34

void MQ7_Init();
void MQ7_Read(float &sensorValue);
void pwmBitBang(int totalMs, int highPct, int cycleMs);