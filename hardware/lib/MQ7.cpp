#include "MQ7.hpp"
 
void MQ7_Init()
{
    pinMode(DIGITAL_MQ7, INPUT);
    pinMode(ANALOG_MQ7, INPUT);
}
 
void MQ7_Read(float &sensorVolt, float &RSAir, float &R0, float &sensorValue)
{
    analogWrite(ANALOG_MQ7, 1023);
    delay(60000);
    analogWrite(ANALOG_MQ7, (1023/5)*1.4);

    for(int i = 0; i<100; i++)
    { 
        sensorValue = sensorValue + analogRead(ANALOG_MQ7);
        delay(90000);
    }

    sensorValue = sensorValue/100.0;
    sensorVolt = sensorValue/1024*5.0;
    RSAir = (5.0-sensorVolt)/sensorVolt;
    R0 = RSAir/(26+(1/3));

    Serial.print("R0 = ");
    Serial.println(R0);

    delay(1000);
}