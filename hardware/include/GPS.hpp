#include "TinyGPSPlus.h"

#define RX 4
#define TX 5

struct GPSData_t
{
    float lat;
    float lon;
};

GPSData_t GPS_Read();