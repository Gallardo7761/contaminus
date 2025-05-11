#include "MqttClient.hpp"

extern WiFiClient wifiClient;

PubSubClient client(wifiClient);

void MQTT_OnReceived(char *topic, byte *payload, unsigned int length)
{
#ifdef DEBUG
  Serial.print("Received on ");
  Serial.print(topic);
  Serial.print(": ");
#endif

  String content = "";

  for (size_t i = 0; i < length; i++)
  {
    content.concat((char)payload[i]);
  }

#ifdef DEBUG
  Serial.println(content);
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
  Serial.print("Starting MQTT connection...");
#endif
  if (client.connect(MQTTClientName, USER, MQTT_PASSWORD))
  {
    String statusTopic = buildTopic(GROUP_ID, String(DEVICE_ID, HEX), "status");
    client.subscribe(statusTopic.c_str());
    client.publish(statusTopic.c_str(), "connected");
  }
#ifdef DEBUG
  Serial.print("Failed MQTT connection, rc=");
  Serial.print(client.state());
  Serial.println(" try again in 5 seconds");
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

String buildTopic(int groupId, const String& deviceId, const String& topic)
{
  String topicString = "group/" + String(groupId) + "/device/" + deviceId + "/" + topic;
  return topicString;
}