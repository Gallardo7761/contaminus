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
import net.miarma.contaminus.common.VoronoiZoneDetector;
import net.miarma.contaminus.entities.Actuator;
import net.miarma.contaminus.entities.COValue;
import net.miarma.contaminus.entities.Device;
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
    private VoronoiZoneDetector detector;

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
    	this.detector = VoronoiZoneDetector.create("https://miarma.net/files/voronoi_sevilla_geovoronoi.geojson", true);
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
        router.route(HttpMethod.GET, Constants.ACTUATOR_STATUS).handler(this::getActuatorStatus);
        router.route(HttpMethod.POST, Constants.ACTUATOR_STATUS).handler(this::postActuatorStatus);
        
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
        String groupId = body.getString("groupId");
        String deviceId = body.getString("deviceId");

        JsonObject gpsJson = body.getJsonObject("gps");
        JsonObject weatherJson = body.getJsonObject("weather");
        JsonObject coJson = body.getJsonObject("co");

        if (groupId == null || deviceId == null || gpsJson == null || weatherJson == null || coJson == null) {
            sendError(context, 400, "Missing required fields");
            return;
        }

        GpsValue gpsValue = gson.fromJson(gpsJson.toString(), GpsValue.class);
        WeatherValue weatherValue = gson.fromJson(weatherJson.toString(), WeatherValue.class);
        COValue coValue = gson.fromJson(coJson.toString(), COValue.class);

        if (!isInCorrectZone(gpsValue, groupId)) {
            sendZoneWarning(context);
            return;
        }

        handleActuators(groupId, coValue.getValue());

        gpsValue.setDeviceId(deviceId);
        weatherValue.setDeviceId(deviceId);
        coValue.setDeviceId(deviceId);

        storeMeasurements(context, groupId, deviceId, gpsValue, weatherValue, coValue);
    }
    
    private void getActuatorStatus(RoutingContext context) {
		String groupId = context.request().getParam("groupId");
		String deviceId = context.request().getParam("deviceId");
		String actuatorId = context.request().getParam("actuatorId");
		
		String host = "http://" + configManager.getHost();
		int port = configManager.getDataApiPort();
		String actuatorPath = Constants.ACTUATOR
			.replace(":groupId", groupId)
			.replace(":deviceId", deviceId)
			.replace(":actuatorId", actuatorId);
				
		restClient.getRequest(port, host, actuatorPath, Actuator.class)
			.onSuccess(actuator -> {
				String actuatorStatus = actuator.getStatus() == 0 ? "Solo vehiculos electricos/hibridos" : "Todo tipo de vehiculos";
				
				context.response()
					.setStatusCode(200)
					.putHeader("Content-Type", "application/json")
					.end(new JsonObject().put("status", "success").put("actuatorStatus", actuatorStatus).encode());
			})
			.onFailure(_ -> sendError(context, 500, "Failed to retrieve actuator status"));
	}
    
    private void postActuatorStatus(RoutingContext context) {
		String groupId = context.request().getParam("groupId");
		String deviceId = context.request().getParam("deviceId");
		String actuatorId = context.request().getParam("actuatorId");

		JsonObject body = context.body().asJsonObject();
		String actuatorStatus = body.getString("status");
		
		String host = "http://" + configManager.getHost();
		int port = configManager.getDataApiPort();
		String actuatorPath = Constants.ACTUATOR
			.replace(":groupId", groupId)
			.replace(":deviceId", deviceId)
			.replace(":actuatorId", actuatorId);

		Actuator updatedActuator = new Actuator(null, null, Integer.valueOf(actuatorStatus), null); // Assuming status 1 is the desired state

		restClient.putRequest(port, host, actuatorPath, updatedActuator, Actuator.class)
			.onSuccess(_ -> {
				context.response()
					.setStatusCode(200)
					.putHeader("Content-Type", "application/json")
					.end(new JsonObject().put("status", "success").put("message", "Actuator status updated").encode());
			})
			.onFailure(_ -> sendError(context, 500, "Failed to update actuator status"));
	}
    
    private void sendError(RoutingContext ctx, int status, String msg) {
        ctx.response().setStatusCode(status).end(msg);
    }

    private boolean isInCorrectZone(GpsValue gps, String expectedZone) {
        Integer actualZone = detector.getZoneForPoint(gps.getLon(), gps.getLat());
        Constants.LOGGER.info(gps.getLat() + ", " + gps.getLon() + " -> Zone: " + actualZone);
        return actualZone.equals(Integer.valueOf(expectedZone));
    }

    private void sendZoneWarning(RoutingContext ctx) {
        Constants.LOGGER.info("El dispositivo no ha medido en su zona");
        ctx.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(new JsonObject().put("status", "success").put("message", "Device did not measure in its zone").encode());
    }

    private void handleActuators(String groupId, float coAmount) {
        String host = "http://" + configManager.getHost();
        int port = configManager.getDataApiPort();
        String devicesPath = Constants.DEVICES.replace(":groupId", groupId);

        restClient.getRequest(port, host, devicesPath, Device[].class)
            .onSuccess(devices -> Arrays.stream(devices)
                .filter(d -> Constants.ACTUATOR_ROLE.equals(d.getDeviceRole()))
                .forEach(d -> {
                    String topic = buildTopic(Integer.parseInt(groupId), d.getDeviceId(), "matrix");
                    publishMQTT(topic, coAmount);

                    String actuatorsPath = Constants.ACTUATORS
                        .replace(":groupId", groupId)
                        .replace(":deviceId", d.getDeviceId());

                    restClient.getRequest(port, host, actuatorsPath, Actuator[].class)
                        .onSuccess(actuators -> Arrays.stream(actuators).forEach(a -> {
                            String actuatorPath = Constants.ACTUATOR
                                .replace(":groupId", groupId)
                                .replace(":deviceId", d.getDeviceId())
                                .replace(":actuatorId", String.valueOf(a.getActuatorId()));
                            Actuator updated = new Actuator(a.getActuatorId(), d.getDeviceId(), coAmount >= 80.0f ? 0 : 1, null);
                            restClient.putRequest(port, host, actuatorPath, updated, Actuator.class);
                        }))
                        .onFailure(err -> Constants.LOGGER.error("Failed to update actuator", err));
                }))
            .onFailure(err -> Constants.LOGGER.error("Failed to retrieve devices", err));
    }

    private void publishMQTT(String topic, float coAmount) {
        if (mqttClient.isConnected()) {
            Constants.LOGGER.info("Publishing to MQTT topic: " + topic);
            mqttClient.publish(topic, Buffer.buffer(coAmount >= 80.0f ? "ECO" : "GAS"),
                MqttQoS.AT_LEAST_ONCE, false, false);
        }
    }

    private void storeMeasurements(RoutingContext ctx, String groupId, String deviceId,
                                   GpsValue gps, WeatherValue weather, COValue co) {

        String host = "http://" + configManager.getHost();
        int port = configManager.getDataApiPort();

        String gpsPath = Constants.ADD_GPS_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);
        String weatherPath = Constants.ADD_WEATHER_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);
        String coPath = Constants.ADD_CO_VALUE.replace(":groupId", groupId).replace(":deviceId", deviceId);

        restClient.postRequest(port, host, gpsPath, gps, GpsValue.class)
            .compose(_ -> restClient.postRequest(port, host, weatherPath, weather, WeatherValue.class))
            .compose(_ -> restClient.postRequest(port, host, coPath, co, COValue.class))
            .onSuccess(_ -> ctx.response()
                .setStatusCode(201)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject().put("status", "success").put("inserted", 3).encode()))
            .onFailure(err -> ctx.fail(500, err));
    }

    
    private String buildTopic(int groupId, String deviceId, String topic) {
      String topicString = "group/" + groupId + "/device/" + deviceId + "/" + topic;
      return topicString;
    }
}