#include "GPS.hpp"

TinyGPSPlus gps;

GPSData_t GPS_Read()
{
    float lat, lon;
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
    return {lat, lon};
}