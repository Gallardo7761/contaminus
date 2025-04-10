#include <Arduino.h>

#define analogMQ7 33
#define digitalMQ7 32

float sensor_volt;
float RS_air; 
float R0;  
float sensorValue;

void MQ7_init(){
    pinMode(digitalMQ7, INPUT);
    pinMode(analogMQ7, INPUT);
}

void MQ7_read()
{
    analogWrite(analogMQ7, 1023);
    delay(60000);
    analogWrite(analogMQ7, (1023/5)*1.4 );

    for(int i = 0; i<100; i++){
        sensorValue = sensorValue + analogRead(analogMQ7);
        delay(90000);
    }

    sensorValue = sensorValue/100.0;
    sensor_volt = sensorValue/1024*5.0;
    RS_air = (5.0-sensor_volt)/sensor_volt;
    R0 = RS_air/(26+(1/3));

    Serial.print("R0 = ");
    Serial.println(R0);
    delay(1000);
}