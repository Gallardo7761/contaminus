#include "GPS.hpp"

TinyGPSPlus gps;
HardwareSerial gpsSerial(1);

void GPS_Init()
{
    gpsSerial.begin(9600, SERIAL_8N1, RX, TX);
}

GPSData_t GPS_Read()
{
    while (gpsSerial.available() > 0) {
        gps.encode(gpsSerial.read());
    }

    float lat = 0.0f, lon = 0.0f;

    if (gps.location.isValid()) {
        lat = gps.location.lat();
        lon = gps.location.lng();
    }

    return {lat, lon};
}
