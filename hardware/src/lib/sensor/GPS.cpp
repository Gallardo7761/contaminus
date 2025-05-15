#include "GPS.hpp"

TinyGPSPlus gps;
HardwareSerial SerialGPS(1);

void GPS_Init()
{
    SerialGPS.begin(9600, SERIAL_8N1, 25, 26); // RX, TX
}

GPSData_t GPS_Read()
{
    while (SerialGPS.available() > 0)
    {
        gps.encode(SerialGPS.read());
    }

    float lat = 0.0f, lon = 0.0f;

    if (gps.location.isUpdated())
    {
        lat = gps.location.lat();
        lon = gps.location.lng();
    }

    return {lat, lon};
}

GPSData_t GPS_Read_Fake()
{
    float rnd = random(-0.005, 0.005);
    return {37.358201f + rnd, -5.986640f + rnd};
}