#include "MQ7.hpp"

void MQ7_Init()
{
  pinMode(HEATER_PIN, OUTPUT);
  pinMode(SENSOR_PIN, INPUT);
}

void MQ7_Read(float &sensorValue)
{
  Serial.println("\t - Calentando MQ7");
  digitalWrite(HEATER_PIN, HIGH);
  delay(60000);

  Serial.println("\t - Enfriando MQ7");
  pwmBitBang(90000, 28, 100);

  const int N = 1;
  long sum = 0;
  for (int i = 0; i < N; i++)
  {
    sum += analogRead(SENSOR_PIN);
    delay(5);
  }

  sensorValue = sum / float(N);
  //sensorVolt = sensorValue * (3.3 / 4095.0); // ADC 12 bits, 3.3V
}

// generamos PWM por software usando bit banging
void pwmBitBang(int totalMs, int highPct, int cycleMs)
{
  int onTime = cycleMs * highPct / 100;
  int offTime = cycleMs - onTime;
  int elapsed = 0;
  
  while (elapsed < totalMs)
  {
    digitalWrite(HEATER_PIN, HIGH);
    delay(onTime);
    digitalWrite(HEATER_PIN, LOW);
    delay(offTime);
    elapsed += cycleMs;
  }
}