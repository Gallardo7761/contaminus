#include "main.hpp"
 
void setup() {
    Serial.begin(9600);

    if(setup_wifi() != 0)
    {
        Serial.print("Error connecting to WiFI");
    }
}
 
void loop() {
 
}