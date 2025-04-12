#include "GPS.hpp"
char c;
SoftwareSerial gps(RX, TX);

void GPS_Init(){
     // RX, TX

    Serial.begin(115500);
    gps.begin(9600);
    Serial.println("GPS Initialized");
}

void GPS_Read(){
    
    if (gps.available()) {
        c = gps.read();
        
    }
}
//$GPRMC,044235.000,A,4322.0289,N,00824.5210,W,0.39,65.46,020615,,,A*44

void GPS_Data(const char* data) {
    char buffer[100];
    strncpy(buffer, data, sizeof(buffer)); // Copia segura de la cadena
    buffer[sizeof(buffer) - 1] = '\0'; // Asegura que la cadena termine en '\0'

    char* token = strtok(buffer, ","); // Divide la cadena por comas
    int index = 0;
    String check;
    float lon = 0.0, lat = 0.0;

    while (token != nullptr) {
        if (index == 2) {
            check = String(token); // Elemento en la posición 2
        } else if (index == 3) {
            lon = atof(token); // Elemento en la posición 3 convertido a float
        } else if (index == 5) {
            lat = atof(token); // Elemento en la posición 5 convertido a float
        }
        token = strtok(nullptr, ","); // Avanza al siguiente token
        index++;
    }

    // Imprime los resultados
    Serial.print("Check: ");
    Serial.println(check);
    Serial.print("Longitude: ");
    Serial.println(lon, 6); // 6 decimales para precisión
    Serial.print("Latitude: ");
    Serial.println(lat, 6);
}