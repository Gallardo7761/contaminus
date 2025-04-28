#include "GPS.hpp"

TinyGPSPlus gps;

void GPS_Init()
{
    Serial.begin(9600);
}

void GPS_Read(float &lat, float &lon)
{
    if (gps.location.isValid())
    {
        lat = gps.location.lat();
        lon = gps.location.lng();
    }
    else
    {
        lat = 0.0f;
        lon = 0.0f;
    }
}