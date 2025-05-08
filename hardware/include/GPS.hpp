#include "TinyGPSPlus.h"

struct GPSData_t
{
    float lat;
    float lon;
};

void GPS_Init();
GPSData_t GPS_Read();