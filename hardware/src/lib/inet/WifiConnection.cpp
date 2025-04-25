#include <WifiConnection.hpp>
 
WiFiClient wifiClient;

int setupWifi()
{
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(SSID);
  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, PASSWORD);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup!");

  if(WiFi.status() == WL_CONNECTED)
  {
    return 0;
  }
  else
  {
    return 1;
  }
}
