#include <WifiConnection.hpp>

#define PIN_R 12
#define PIN_G 13
#define PIN_B 14

WiFiClient wifiClient;

void setColor(uint8_t r, uint8_t g, uint8_t b)
{
  ledcWrite(0, r);
  ledcWrite(1, g);
  ledcWrite(2, b);
}

void setupLED()
{
  ledcSetup(0, 5000, 8);
  ledcAttachPin(PIN_R, 0);

  ledcSetup(1, 5000, 8);
  ledcAttachPin(PIN_G, 1);

  ledcSetup(2, 5000, 8);
  ledcAttachPin(PIN_B, 2);
}

// hue cycle
void hueCycle(uint8_t pos)
{
  uint8_t r = (uint8_t)(sin((pos + 0) * 0.024) * 127 + 128);
  uint8_t g = (uint8_t)(sin((pos + 85) * 0.024) * 127 + 128);
  uint8_t b = (uint8_t)(sin((pos + 170) * 0.024) * 127 + 128);
  setColor(r, g, b);
}

int setupWifi()
{
  setupLED();

  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, PASSWORD);

#ifdef DEBUG
  Serial.print("Conectando a la red WiFi: ");
  Serial.print(SSID);
#endif

  int hue = 0;
  uint32_t start = millis();
  const uint32_t timeout = 15000;

  while (WiFi.status() != WL_CONNECTED && millis() - start < timeout)
  {
    hueCycle(hue++);

#ifdef DEBUG
    Serial.print(".");
#endif

    delay(30);
  }

  if (WiFi.status() == WL_CONNECTED)
  {
    setColor(0, 255, 0);

#ifdef DEBUG
    Serial.println("Conectado a la red WiFi");
    Serial.print("Dirección IP: ");
    Serial.println(WiFi.localIP());
#endif

    return 0;
  }
  else
  {
    setColor(255, 0, 0);

#ifdef DEBUG
    Serial.println("No se pudo conectar a la red WiFi");
#endif

    return 1;
  }
}