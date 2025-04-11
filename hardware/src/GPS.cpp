#include "GPS.h"
char c = "";


void GPS_Init(){
    SoftwareSerial gps(RX, TX); // RX, TX

    Serial.begin(9600);
    gps.begin(9600);
    Serial.println("GPS Initialized");
}

void GPS_Read(){
    
    if (gps.available()) {
        c = gps.read();
        
    }
}

String GPS_Data(char data){
    char[] c = data.split(",");
    float lon = c[2];
    
}