package net.miarma.contaminus.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.database.entities.DeviceSensorValue;

public class MqttVerticle extends AbstractVerticle  {
	private final Gson gson = new Gson();
	private final ConfigManager configManager;
	
	public MqttVerticle() {
		this.configManager = ConfigManager.getInstance();
	}
	
	@Override 
	public void start(Promise<Void> startPromise) {
		MqttClient mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));
		mqttClient.connect(
			configManager.getIntProperty("mqtt.port"),
			configManager.getStringProperty("inet.host"), _ -> {
				mqttClient.subscribe("device_measures", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if(handler.succeeded()) {
						System.out.println("Suscripción: " + mqttClient.clientId());
					}
				});
				
				mqttClient.publishHandler(handler -> {
					System.out.println("Mensaje recibido: ");
					System.out.println("	Topic: " + handler.topicName().toString());
					System.out.println("	ID del mensaje: " + handler.messageId());
					System.out.println("	Contenido: " + handler.payload().toString());
					try {
						DeviceSensorValue dsv = gson.fromJson(handler.payload().toString(), DeviceSensorValue.class);
						System.out.println("	DeviceSensorValue: " + dsv.toString());
					} catch(JsonSyntaxException e) {
						System.out.println("	No es un DeviceSensorValue");
					}
				});
				
				mqttClient.publish("device_measures", Buffer.buffer("Ejemplo :v"), 
						MqttQoS.AT_LEAST_ONCE, false, false);
			});
	}
}
