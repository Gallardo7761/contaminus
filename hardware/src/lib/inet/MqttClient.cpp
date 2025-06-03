#include "MqttClient.hpp"

extern WiFiClient wifiClient;
extern HTTPClient httpClient;

PubSubClient client(wifiClient);

void MQTT_OnReceived(char *topic, byte *payload, unsigned int length)
{
#ifdef DEBUG
  Serial.print("📥 Received on ");
  Serial.print(topic);
  Serial.print(": ");
#endif

  String content = "";

  for (size_t i = 0; i < length; i++)
  {
    content.concat((char)payload[i]);
  }

  content.trim();

#ifdef DEBUG
  Serial.println(content);
#endif

#if DEVICE_ROLE == ACTUATOR
  if (content.equals("ECO"))
  {
    currentMessage = "Vehiculos electricos/hibridos";
  }
  else if (content.equals("GAS"))
  {
    currentMessage = "Todo tipo de vehiculos";
  }
#endif
}

void MQTT_Init(const char *MQTTServerAddress, uint16_t MQTTServerPort)
{
  client.setServer(MQTTServerAddress, MQTTServerPort);
  client.setCallback(MQTT_OnReceived);
}

void MQTT_Connect(const char *MQTTClientName)
{
#ifdef DEBUG
  Serial.println("🔌 Starting MQTT connection...");
#endif
  if (client.connect(MQTTClientName, USER, MQTT_PASSWORD))
  {
    String statusTopic = buildTopic(GROUP_ID, String(DEVICE_ID, HEX), "status");
    String matrixTopic = buildTopic(GROUP_ID, String(DEVICE_ID, HEX), "matrix");
    client.subscribe(statusTopic.c_str());
    client.subscribe(matrixTopic.c_str());
    client.publish(statusTopic.c_str(), "connected");
  }
#ifdef DEBUG
  Serial.println("❌ Failed MQTT connection, trying again in 5 seconds");
#endif
}

void MQTT_Handle(const char *MQTTClientName)
{
  if (!client.connected())
  {
    MQTT_Connect(MQTTClientName);
  }
  client.loop();
}

String buildTopic(int groupId, const String &deviceId, const String &topic)
{
  String topicString = "group/" + String(groupId) + "/device/" + deviceId + "/" + topic;
  return topicString;
}