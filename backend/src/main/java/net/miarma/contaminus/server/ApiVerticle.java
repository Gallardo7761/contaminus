package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.Host;
import net.miarma.contaminus.database.QueryBuilder;

public class ApiVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        Constants.LOGGER.info("🟢 Iniciando ApiVerticle...");
        Router router = Router.router(vertx);
        
        Set<HttpMethod> allowedMethods = new HashSet<>(
        		Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.OPTIONS)); // Por ejemplo
		Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));

        router.route().handler(CorsHandler.create()
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));
        router.route().handler(BodyHandler.create());
        
        // Group Routes
        router.route(HttpMethod.GET, Constants.GET_GROUPS).handler(this::getGroupsHandler);
        router.route(HttpMethod.GET, Constants.GET_GROUP_BY_ID).handler(this::getGroupByIdHandler);
        router.route(HttpMethod.GET, Constants.GET_GROUP_DEVICES).handler(this::getGroupDevicesHandler);
        router.route(HttpMethod.POST, Constants.POST_GROUPS).handler(this::postGroupHandler);
        router.route(HttpMethod.PUT, Constants.PUT_GROUP_BY_ID).handler(this::putGroupByIdHandler);

        // Device Routes
        router.route(HttpMethod.GET, Constants.GET_DEVICES).handler(this::getDevicesHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_BY_ID).handler(this::getDeviceByIdHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_SENSORS).handler(this::getDeviceSensorsHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_ACTUATORS).handler(this::getDeviceActuatorsHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_LATEST_VALUES).handler(this::getDeviceLatestValuesHandler);
        router.route(HttpMethod.POST, Constants.POST_DEVICES).handler(this::postDeviceHandler);
        router.route(HttpMethod.PUT, Constants.PUT_DEVICE_BY_ID).handler(this::putDeviceByIdHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_POLLUTION_MAP).handler(this::getPollutionMapHandler);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_HISTORY).handler(this::getDeviceHistoryHandler);

        // Sensor Routes
        router.route(HttpMethod.GET, Constants.GET_SENSORS).handler(this::getSensorsHandler);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_BY_ID).handler(this::getSensorByIdHandler);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_VALUES).handler(this::getSensorValuesHandler);
        router.route(HttpMethod.POST, Constants.POST_SENSORS).handler(this::postSensorHandler);
        router.route(HttpMethod.PUT, Constants.PUT_SENSOR_BY_ID).handler(this::putSensorByIdHandler);

        // Actuator Routes
        router.route(HttpMethod.GET, Constants.GET_ACTUATORS).handler(this::getActuatorsHandler);
        router.route(HttpMethod.GET, Constants.GET_ACTUATOR_BY_ID).handler(this::getActuatorByIdHandler);
        router.route(HttpMethod.POST, Constants.POST_ACTUATORS).handler(this::postActuatorHandler);
        router.route(HttpMethod.PUT, Constants.PUT_ACTUATOR_BY_ID).handler(this::putActuatorByIdHandler);

        vertx.createHttpServer()
	        .requestHandler(router)
	        .listen(
                Host.getApiPort(),
                Host.getHost(),
                result -> {
	                if (result.succeeded()) {
	                    Constants.LOGGER.info(String.format(
	                        "📡 ApiVerticle desplegado. (http://%s:%d)", Host.getHost(), Host.getApiPort()
	                    ));
	                    startPromise.complete();
	                } else {
	                    Constants.LOGGER.error("❌ Error al desplegar ApiVerticle", result.cause());
	                    startPromise.fail(result.cause());
	                }
            });
    }
       
    private void sendQuery(String query, RoutingContext context) {
        vertx.eventBus().request("db.query", query, req -> {
            if (req.succeeded()) {
                Message<Object> msg = req.result();
                JsonArray jsonArray = new JsonArray(msg.body().toString());
                context.json(jsonArray);
            } else {
                context.fail(500, req.cause());
            }
        });
    }

    
    // Group Handlers
    private void getGroupsHandler(RoutingContext context) {
    	String query = QueryBuilder.select("*").from("groups").build();
    	sendQuery(query, context);
    }

    private void getGroupByIdHandler(RoutingContext context) {
        String groupId = context.request().getParam("groupId");
        String query = QueryBuilder
        		.select("*")
        		.from("groups")
        		.where("groupId = ?", groupId)
        		.build();
        sendQuery(query, context);
    }
    
    private void getGroupDevicesHandler(RoutingContext context) {
    	String groupId = context.request().getParam("groupId");
		String query = QueryBuilder
				.select("*")
				.from("devices")
				.where("groupId = ?", groupId)
				.build();
		sendQuery(query, context);
    }

    private void postGroupHandler(RoutingContext context) {
        JsonObject body = context.body().asJsonObject();
        
        if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
        
        String groupName = body.getString("groupName");
        
        String query = QueryBuilder
        		.insert("groups", "groupName")
        		.values(groupName)
        		.build();
        
        sendQuery(query, context);
    }

    private void putGroupByIdHandler(RoutingContext context) {
        String groupId = context.request().getParam("groupId");
		JsonObject body = context.body().asJsonObject();
		
		if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
		
		String groupName = body.getString("groupName");
		
		String query = QueryBuilder
				.update("groups")
				.set("groupName", groupName)
				.where("groupId = ?", groupId)
				.build();
		
		sendQuery(query, context);
    }

    // Device Handlers
    private void getDevicesHandler(RoutingContext context) {
    	String query = QueryBuilder.select("*").from("devices").build();
    	sendQuery(query, context);
    }

    private void getDeviceByIdHandler(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
        String query = QueryBuilder
        		.select("*")
        		.from("devices")
        		.where("deviceId = ?", deviceId)
        		.build();
        sendQuery(query, context);
    }
    
    private void getDeviceSensorsHandler(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
		String query = QueryBuilder
				.select("*")
				.from("sensors")
				.where("deviceId = ?", deviceId)
				.build();
		sendQuery(query, context);
	}
    
    private void getDeviceActuatorsHandler(RoutingContext context) {
		String deviceId = context.request().getParam("deviceId");
		String query = QueryBuilder
				.select("*")
				.from("actuators")
				.where("deviceId = ?", deviceId)
				.build();
		sendQuery(query, context);
	}
    
    private void getDeviceLatestValuesHandler(RoutingContext context) {
		String deviceId = context.request().getParam("deviceId");
		String query = QueryBuilder
				.select("*")
				.from("v_latest_values")
				.where("deviceId = ?", deviceId)
				.build();
		sendQuery(query, context);
    }

    private void postDeviceHandler(RoutingContext context) {
    	JsonObject body = context.body().asJsonObject();
        
        if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
        
        Integer groupId = body.getInteger("groupId");
        String deviceName = body.getString("groupName");
        
        String query = QueryBuilder
        		.insert("devices", "groupId", "deviceName")
        		.values(groupId, deviceName)
        		.build();
        
        sendQuery(query, context);
    }

    private void putDeviceByIdHandler(RoutingContext context) {
		String deviceId = context.request().getParam("deviceId");
		JsonObject body = context.body().asJsonObject();
		
		if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
		
		Integer groupId = body.getInteger("groupId");
		String deviceName = body.getString("deviceName");
		
		String query = QueryBuilder
				.update("devices")
				.set("groupId", groupId)
				.set("deviceName", deviceName)
				.where("deviceId = ?", deviceId)
				.build();
		
		sendQuery(query, context);
    }
    
    private void getPollutionMapHandler(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
		String query = QueryBuilder
				.select("*")
				.from("v_pollution_map")
				.where("deviceId = ?", deviceId)
				.build();
		sendQuery(query, context);
    }
    
    private void getDeviceHistoryHandler(RoutingContext context) {
		String deviceId = context.request().getParam("deviceId");
		String query = QueryBuilder
				.select("*")
				.from("v_sensor_history_by_device")
				.where("deviceId = ?", deviceId)
				.build();
		sendQuery(query, context);
	}

    // Sensor Handlers
    private void getSensorsHandler(RoutingContext context) {
    	String query = QueryBuilder.select("*").from("sensors").build();
    	sendQuery(query, context);
    }

    private void getSensorByIdHandler(RoutingContext context) {
        String sensorId = context.request().getParam("sensorId");
        String query = QueryBuilder
        		.select("*")
        		.from("sensors")
        		.where("sensorId = ?", sensorId)
        		.build();
        sendQuery(query, context);
    }
    
    private void getSensorValuesHandler(RoutingContext context) {
    	String sensorId = context.request().getParam("sensorId");
		String query = QueryBuilder
				.select("*")
				.from("v_sensor_values")
				.where("sensorId = ?", sensorId)
				.build();
		sendQuery(query, context);
    }

    private void postSensorHandler(RoutingContext context) {
    	JsonObject body = context.body().asJsonObject();
        
        if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
        
        Integer deviceId = body.getInteger("deviceId");
        String sensorType = body.getString("sensorType");
        String unit = body.getString("unit");
        Integer status = body.getInteger("status");
        
        String query = QueryBuilder
				.insert("sensors", "deviceId", "sensorType", "unit", "status")
				.values(deviceId, sensorType, unit, status)
				.build();
		
		sendQuery(query, context);
    }

    private void putSensorByIdHandler(RoutingContext context) {
		String sensorId = context.request().getParam("sensorId");
		JsonObject body = context.body().asJsonObject();
		
		if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
		
		Integer deviceId = body.getInteger("deviceId");
		String sensorType = body.getString("sensorType");
		String unit = body.getString("unit");
		Integer status = body.getInteger("status");
		
		String query = QueryBuilder
				.update("sensors")
				.set("deviceId", deviceId)
				.set("sensorType", sensorType)
				.set("unit", unit)
				.set("status", status)
				.where("sensorId = ?", sensorId)
				.build();
		
		sendQuery(query, context);
    }

    // Actuator Handlers
    private void getActuatorsHandler(RoutingContext context) {
    	String query = QueryBuilder.select("*").from("actuators").build();
    	sendQuery(query, context);
    }

    private void getActuatorByIdHandler(RoutingContext context) {
        String actuatorId = context.request().getParam("actuatorId");
        
        String query = QueryBuilder
				.select("*")
				.from("actuators")
				.where("actuatorId = ?", actuatorId)
				.build();	
        
        sendQuery(query, context);
    }

    private void postActuatorHandler(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		
		if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
		
		Integer deviceId = body.getInteger("deviceId");
		Integer status = body.getInteger("status");
		
		String query = QueryBuilder
				.insert("actuators", "deviceId", "status")
				.values(deviceId, status)
				.build();
		
		sendQuery(query, context);
    }

    private void putActuatorByIdHandler(RoutingContext context) {
		String actuatorId = context.request().getParam("actuatorId");
		JsonObject body = context.body().asJsonObject();
        
		if(body == null) {
			context.fail(400, new IllegalArgumentException("Bad request"));
			return;
		}
		
		Integer deviceId = body.getInteger("deviceId");
		Integer status = body.getInteger("status");
		
		String query = QueryBuilder
				.update("actuators")
				.set("deviceId", deviceId)
				.set("status", status)
				.where("actuatorId = ?", actuatorId)
				.build();
		
		sendQuery(query, context);
    }
    
}