#include <WifiConnection.hpp>

WiFiClient wifiClient;
bool wifiConnected = false;
TaskTimer_t wifiTimer{0, 1000};

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

void hueCycle(uint8_t pos)
{
  uint8_t r = (uint8_t)(sin((pos + 0) * 0.024) * 127 + 128);
  uint8_t g = (uint8_t)(sin((pos + 85) * 0.024) * 127 + 128);
  uint8_t b = (uint8_t)(sin((pos + 170) * 0.024) * 127 + 128);
  setColor(r, g, b);
}

void WiFi_Init()
{
  setupLED();
  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, WIFI_PASSWORD);
#ifdef DEBUG
  Serial.print("🟡 Trying to connect to WiFi: ");
  Serial.println(SSID);
#endif
}

bool WiFi_IsConnected()
{
  return wifiConnected;
}

void WiFi_Handle()
{
  static uint8_t hue = 0;
  uint32_t now = millis();

  if (!wifiConnected)
  {
    hueCycle(hue++);
  }

  if (WiFi.status() == WL_CONNECTED)
  {
    if (!wifiConnected)
    {
#ifdef DEBUG
      Serial.print("🟢 Connected to WiFi. IP: ");  
      Serial.print(WiFi.localIP());
      Serial.print(" | SSID: ");
      Serial.println(SSID);
#endif
      setColor(0, 255, 0);
      wifiConnected = true;
    }
    return;
  }

  if (now - wifiTimer.lastRun >= wifiTimer.interval)
  {
#ifdef DEBUG
    Serial.println("🔁 Retrying WiFi connection...");
#endif
    WiFi.disconnect(true);
    WiFi.begin(SSID, WIFI_PASSWORD);
    wifiTimer.lastRun = now;
  }
}