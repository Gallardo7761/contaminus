package net.miarma.contaminus.verticles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.entities.COValue;
import net.miarma.contaminus.entities.GpsValue;
import net.miarma.contaminus.entities.ViewLatestValues;
import net.miarma.contaminus.entities.ViewPollutionMap;
import net.miarma.contaminus.entities.ViewSensorHistory;
import net.miarma.contaminus.entities.ViewSensorValue;
import net.miarma.contaminus.entities.WeatherValue;
import net.miarma.contaminus.util.RestClientUtil;

public class LogicLayerAPIVerticle extends AbstractVerticle {
    private ConfigManager configManager;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private RestClientUtil restClient;
    private MqttClient mqttClient;

    public LogicLayerAPIVerticle() {
    	this.configManager = ConfigManager.getInstance();
    	WebClientOptions options = new WebClientOptions()
    			.setUserAgent("ContaminUS");
    	this.restClient = new RestClientUtil(WebClient.create(Vertx.vertx(), options));
    	this.mqttClient = MqttClient.create(Vertx.vertx(), 
    			new MqttClientOptions()
					.setAutoKeepAlive(true)
					.setUsername("contaminus")
					.setPassword("contaminus")
		);
    }   
    
    @Override
    public void start(Promise<Void> startPromise) {    	
        Constants.LOGGER.info("📡 Iniciando LogicApiVerticle...");
		
        Router router = Router.router(vertx);
	    Set<HttpMethod> allowedMethods = new HashSet<>(
	    		Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.OPTIONS));
		Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));

        router.route().handler(CorsHandler.create()
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));
        
        router.route().handler(BodyHandler.create());
        
        router.route(HttpMethod.POST, Constants.BATCH).handler(this::addBatch);
        router.route(HttpMethod.GET, Constants.LATEST_VALUES).handler(this::getDeviceLatestValues);
        router.route(HttpMethod.GET, Constants.POLLUTION_MAP).handler(this::getDevicePollutionMap);
        router.route(HttpMethod.GET, Constants.HISTORY).handler(this::getDeviceHistory);
        router.route(HttpMethod.GET, Constants.SENSOR_VALUES).handler(this::getSensorValues);

        mqttClient.connect(1883, "localhost", ar -> {
            if (ar.succeeded()) {
            	Constants.LOGGER.info("🟢 MQTT client connected");
                vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(configManager.getLogicApiPort(), configManager.getHost(), http -> {
                        if (http.succeeded()) {
                        	Constants.LOGGER.info("🟢 HTTP server started on port " + configManager.getLogicApiPort());
                            startPromise.complete();
                        } else {
                        	Constants.LOGGER.error("🔴 HTTP server failed to start: " + http.cause());
                            startPromise.fail(http.cause());
                        }
                    });
            } else {
            	Constants.LOGGER.error("🔴 MQTT client connection failed: " + ar.cause());
                startPromise.fail(ar.cause());
            }
        });
    }
           
    private void getDeviceLatestValues(RoutingContext context) {
        String deviceId = context.request().getParam("deviceId");
        restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
                Constants.VIEW_LATEST_VALUES, ViewLatestValues[].class)
            .onSuccess(result -> {
                List<ViewLatestValues> aux = Arrays.stream(result)
                    .filter(elem -> deviceId.equals(elem.getDeviceId()))
                    .toList();
                context.response().putHeader("content-type", "application/json; charset=utf-8").end(gson.toJson(aux));
            })
            .onFailure(err -> context.fail(500, err));
    }

    private void getDevicePollutionMap(RoutingContext context) {
        String deviceId = context.request().getParam("deviceId");
        restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
                Constants.VIEW_POLLUTION_MAP, ViewPollutionMap[].class)
            .onSuccess(result -> {
                List<ViewPollutionMap> aux = Arrays.asList(result).stream()
                    .filter(elem -> deviceId.equals(elem.getDeviceId()))
                    .toList();
                context.response().putHeader("content-type", "application/json; charset=utf-8").end(gson.toJson(aux));
            })
            .onFailure(err -> context.fail(500, err));
    }

    private void getDeviceHistory(RoutingContext context) {
        String deviceId = context.request().getParam("deviceId");
        restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
                Constants.VIEW_SENSOR_HISTORY, ViewSensorHistory[].class)
            .onSuccess(result -> {
                List<ViewSensorHistory> aux = Arrays.asList(result).stream()
                    .filter(elem -> deviceId.equals(elem.getDeviceId()))
                    .toList();
                context.response().putHeader("content-type", "application/json; charset=utf-8").end(gson.toJson(aux));
            })
            .onFailure(err -> context.fail(500, err));
    }

    private void getSensorValues(RoutingContext context) {
        int sensorId = Integer.parseInt(context.request().getParam("sensorId"));
        restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
                Constants.VIEW_SENSOR_VALUES, ViewSensorValue[].class)
            .onSuccess(result -> {
                List<ViewSensorValue> aux = Arrays.asList(result).stream()
                    .filter(val -> val.getSensorId() == sensorId)
                    .toList();
                context.response().putHeader("content-type", "application/json; charset=utf-8").end(gson.toJson(aux));
            })
            .onFailure(err -> context.fail(500, err));
    }
    
    private void addBatch(RoutingContext context) {
        JsonObject body = context.body().asJsonObject();
        if (body == null) {
            context.response().setStatusCode(400).end("Missing JSON body");
            return;
        }

        String groupId = body.getString("groupId");
        String deviceId = body.getString("deviceId");

        JsonObject gps = body.getJsonObject("gps");
        JsonObject weather = body.getJsonObject("weather");
        JsonObject co = body.getJsonObject("co");

        if (deviceId == null || gps == null || weather == null || co == null) {
            context.response().setStatusCode(400).end("Missing required fields");
            return;
        }

        GpsValue gpsValue = gson.fromJson(gps.toString(), GpsValue.class);
        WeatherValue weatherValue = gson.fromJson(weather.toString(), WeatherValue.class);
        COValue coValue = gson.fromJson(co.toString(), COValue.class);
        
        // MQTT publish =============================
        float coAmount = coValue.getValue();
        Constants.LOGGER.info("CO amount received: " + coAmount);
        String topic = buildTopic(Integer.parseInt(groupId), deviceId, "matrix");
        Constants.LOGGER.info("Topic: " + topic);
        if (mqttClient.isConnected()) {
			Constants.LOGGER.info("🟢 Publishing to MQTT topic: " + topic + " with value: " + coAmount);
            mqttClient.publish(topic, Buffer.buffer(coAmount >= 80.0f ? "ECO" : "GAS"),
                MqttQoS.AT_LEAST_ONCE, false, false);
            Constants.LOGGER.info("🟢 Message published to MQTT topic: " + topic);
        }
        // ============================================
        
        gpsValue.setDeviceId(deviceId);
        weatherValue.setDeviceId(deviceId);
        coValue.setDeviceId(deviceId);

        String host = "http://" + configManager.getHost();
        int port = configManager.getDataApiPort();

        String gpsPath = Constants.ADD_GPS_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);
        String weatherPath = Constants.ADD_WEATHER_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);
        String coPath = Constants.ADD_CO_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);

        restClient.postRequest(port, host, gpsPath, gpsValue, GpsValue.class)
            .compose(_ -> restClient.postRequest(port, host, weatherPath, weatherValue, WeatherValue.class))
            .compose(_ -> restClient.postRequest(port, host, coPath, coValue, COValue.class))
            .onSuccess(_ -> {
                context.response()
                    .setStatusCode(201)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("status", "success").put("inserted", 3).encode());
            })
            .onFailure(err -> context.fail(500, err));
    }
    
    private String buildTopic(int groupId, String deviceId, String topic)
    {
      String topicString = "group/" + groupId + "/device/" + deviceId + "/" + topic;
      return topicString;
    }
}