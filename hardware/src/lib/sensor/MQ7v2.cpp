#include "MQ7v2.hpp"

MQ7State_t mq7State = HEATING;
uint32_t mq7StateStart = 0; // t_0
float lastPPM = 0.0f;
float mq7Samples[MAX_SAMPLES];
uint8_t sampleIndex = 0;

void MQ7_Init()
{
    pinMode(MQ7_PIN, INPUT);
    pinMode(RELAY_CONTROL_PIN, OUTPUT);
    mq7State = HEATING;
    mq7StateStart = millis(); // t_0
    digitalWrite(RELAY_CONTROL_PIN, LOW); // NC
}

bool MQ7_Update()
{
    unsigned long now = millis();

    switch (mq7State) {
        case HEATING:
            if (now - mq7StateStart >= 60000) {
                digitalWrite(RELAY_CONTROL_PIN, HIGH); // NO - lectura
                mq7State = SAMPLING;
                mq7StateStart = now;
            }
            break;

        case SAMPLING:
            static uint32_t lastSampleTime = 0;
            const uint32_t sampleInterval = 1000; // 1 muestra/segundo

            if(now - lastSampleTime >= sampleInterval && 
                sampleIndex < MAX_SAMPLES)
            {
                lastSampleTime = now;   

                int adcValue = analogRead(MQ7_PIN);
                float voltage = (adcValue / ADC_RES) * VCC;

                if (voltage > 0.1)
                {
                    float rs = (VCC - voltage) * RL / voltage;
                    float ratio = rs / RO;
                    float ppm = pow(10, (-1.5 * log10(ratio) + 0.8));
                    mq7Samples[sampleIndex++] = ppm;
                } 
                else
                {
                    mq7Samples[sampleIndex++] = 0.0f;
                }
            }

            if(now - mq7StateStart >= 90000) 
            {
                float sum = 0;
                for(uint8_t i = 0; i < sampleIndex; i++)
                {
                    sum += mq7Samples[i];
                }

                lastPPM = (sampleIndex > 0) ? (sum / sampleIndex) : 0.0f;

                sampleIndex = 0;
                digitalWrite(RELAY_CONTROL_PIN, LOW); // NC
                mq7State = HEATING;
                mq7StateStart = now; // t_0
                return true;
            }
            break;
    }

    return false;
}

MQ7Data_t MQ7_Read()
{
    return {lastPPM};
}