#include "MQ7.hpp"

void MQ7_Init()
{
    pinMode(DIGITAL_MQ7, INPUT);
    pinMode(ANALOG_MQ7, INPUT);
}
 
void MQ7_Read(float &sensorVolt, float &RSAir, float &R0, float &sensorValue)
{
    // preheat
    analogWrite(ANALOG_MQ7, 1023);
    delay(60000);
    analogWrite(ANALOG_MQ7, (1023/5)*1.4 );

    for(int i = 0; i<100; i++)
    { 
        sensorValue = sensorValue + analogRead(ANALOG_MQ7);
        delay(1200);
    }

    sensorValue = sensorValue/100.0;
    sensorVolt = sensorValue/1024*5.0;
    RSAir = (5.0-sensorVolt)/sensorVolt;
    R0 = RSAir/(26+(1/3));
}