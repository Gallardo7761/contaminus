#include "main.hpp"
 
const char* ssid = "SiempreHome";
const char* password = "d7?a35D9EnaPepXY?c!4";
const char* mqtt_server = "192.168.0.155";
 
WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
 
void setup() {

}
 
void loop() {
 
}