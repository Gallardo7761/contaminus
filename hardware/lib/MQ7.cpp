#include "MQ7.hpp"
 
unsigned long tiempoAnterior = 0;
const long intervalo1 = 60000;
const long intervalo2 = 3000;

void MQ7_Init()
{
    pinMode(DIGITAL_MQ7, INPUT);
    pinMode(ANALOG_MQ7, INPUT);
}
 
void MQ7_Read(float &sensorVolt, float &RSAir, float &R0, float &sensorValue)
{
    unsigned long tiempoActual = millis();

    if (tiempoActual - tiempoAnterior >= intervalo1){
        tiempoAnterior = tiempoActual;
        analogWrite(ANALOG_MQ7, 1023);
        analogWrite(ANALOG_MQ7, (1023/5)*1.4);
    }
    for(int i = 0; i<50; i++)
    {  
        if (tiempoActual - tiempoAnterior >= intervalo1){
            tiempoAnterior = tiempoActual;
            sensorValue = sensorValue + analogRead(ANALOG_MQ7);
        }
    }
    
    if (tiempoActual - tiempoAnterior >= intervalo1){
        tiempoAnterior = tiempoActual;
        sensorValue = sensorValue/100.0;
        sensorVolt = sensorValue/1024*5.0;
        RSAir = (5.0-sensorVolt)/sensorVolt;
        R0 = RSAir/(26+(1/3));
    
        Serial.print("R0 = ");
        Serial.println(R0);
    }
}