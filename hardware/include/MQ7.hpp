#include <Arduino.h>
 
#define ANALOG_MQ7 33
#define DIGITAL_MQ7 32
 
void MQ7_Init();
void MQ7_Read(float &sensorVolt, float &RSAir, float &R0, float &sensorValue);