#include "mqtt.hpp"

// MQTT configuration
WiFiClient espClient;
PubSubClient client(espClient);

void OnMqttReceived(char *topic, byte *payload, unsigned int length)
{
    Serial.print("Received on ");
    Serial.print(topic);
    Serial.print(": ");

    String content = "";

    for (size_t i = 0; i < length; i++) {
        content.concat((char)payload[i]);
    }

    Serial.print(content);
    Serial.println();
}

void InitMqtt(const char * MQTTServerAddress, uint16_t MQTTServerPort)
{
  client.setServer(MQTTServerAddress, MQTTServerPort);
  client.setCallback(OnMqttReceived);
}

// conecta o reconecta al MQTT
// consigue conectar -> suscribe a topic y publica un mensaje
// no -> espera 5 segundos
void ConnectMqtt(const char * MQTTClientName)
{
  Serial.print("Starting MQTT connection...");
  if (client.connect(MQTTClientName))
  {
    client.subscribe("hello/world");
    client.publish("hello/world", "connected");
  }
  else
  {
    Serial.print("Failed MQTT connection, rc=");
    Serial.print(client.state());
    Serial.println(" try again in 5 seconds");

    delay(5000);
  }
}

// gestiona la comunicación MQTT
// comprueba que el cliente está conectado
// no -> intenta reconectar
// si -> llama al MQTT loop
void HandleMqtt(const char * MQTTClientName)
{
  if (!client.connected())
  {
    ConnectMqtt(MQTTClientName);
  }
  client.loop();
}
