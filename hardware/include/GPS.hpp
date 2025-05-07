#include "TinyGPSPlus.h"

#define RX 26
#define TX 14

struct GPSData_t
{
    float lat;
    float lon;
};

void GPS_Init();
GPSData_t GPS_Read();