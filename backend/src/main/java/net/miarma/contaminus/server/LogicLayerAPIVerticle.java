package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.entities.Actuator;
import net.miarma.contaminus.database.entities.Device;
import net.miarma.contaminus.database.entities.DeviceLatestValuesView;
import net.miarma.contaminus.database.entities.DevicePollutionMap;
import net.miarma.contaminus.database.entities.DeviceSensorHistory;
import net.miarma.contaminus.database.entities.DeviceSensorValue;
import net.miarma.contaminus.database.entities.Sensor;
import net.miarma.contaminus.util.RestClientUtil;

public class LogicLayerAPIVerticle extends AbstractVerticle {
    private ConfigManager configManager;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private RestClientUtil restClient;

    public LogicLayerAPIVerticle() {
    	this.configManager = ConfigManager.getInstance();
    	WebClientOptions options = new WebClientOptions()
    			.setUserAgent("ContaminUS");
    	this.restClient = new RestClientUtil(WebClient.create(Vertx.vertx(), options));
    }   
    
    @Override
    public void start(Promise<Void> startPromise) {    	
        Constants.LOGGER.info("📡 Iniciando LogicApiVerticle...");
		
        Router router = Router.router(vertx);
	    Set<HttpMethod> allowedMethods = new HashSet<>(
	    		Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.OPTIONS)); // Por ejemplo
		Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));

        router.route().handler(CorsHandler.create()
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));
        
        router.route().handler(BodyHandler.create());
        
        router.route(HttpMethod.GET, Constants.GET_GROUP_DEVICES).handler(this::getGroupDevices);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_SENSORS).handler(this::getDeviceSensors);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_ACTUATORS).handler(this::getDeviceActuators);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_LATEST_VALUES).handler(this::getDeviceLatestValues);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_POLLUTION_MAP).handler(this::getDevicePollutionMap);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_HISTORY).handler(this::getDeviceHistory);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_VALUES).handler(this::getSensorValues);

        vertx.createHttpServer()
	        .requestHandler(router)
	        .listen(configManager.getLogicApiPort(), configManager.getHost());
        
        startPromise.complete();
    }
       
    private void getGroupDevices(RoutingContext context) {
        Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
        
        Promise<Device[]> resultList = Promise.promise();
        resultList.future().onComplete(complete -> {
            if(complete.succeeded()) {
                List<Device> aux = Stream.of(complete.result())
                        .filter(d -> d.getGroupId() == groupId)
                        .toList();
                context.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(gson.toJson(aux));
            } else {
                context.fail(500, complete.cause());
            }
        });
        
        this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
                Constants.GET_DEVICES, Device[].class, resultList);
    }
    
    private void getDeviceSensors(RoutingContext context) {
    	Integer deviceId = Integer.parseInt(context.request().getParam("deviceId"));
    	Promise<Sensor[]> resultList = Promise.promise();
    	resultList.future().onComplete(result -> {
    	    if (result.succeeded()) {
    	    	Sensor[] sensors = result.result();
    	        List<Sensor> aux = Arrays.stream(sensors)
    	            .filter(s -> s.getDeviceId() == deviceId)
    	            .toList();
    	        context.response().putHeader("Content-Type", "application/json").end(gson.toJson(aux));
    	    } else {
    	        context.response().setStatusCode(500).end(result.cause().getMessage());
    	    }
    	});

    	restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(), 
    			Constants.GET_SENSORS, Sensor[].class, resultList);
    }
    
    private void getDeviceActuators(RoutingContext context) {
    	Integer deviceId = Integer.parseInt(context.request().getParam("deviceId"));
    	Promise<Actuator[]> resultList = Promise.promise();
    	resultList.future().onComplete(result -> {
    	    if (result.succeeded()) {
    	    	Actuator[] devices = result.result();
    	        List<Actuator> aux = Arrays.stream(devices)
    	            .filter(a -> a.getDeviceId() == deviceId)
    	            .toList();
    	        context.response().putHeader("Content-Type", "application/json").end(gson.toJson(aux));
    	    } else {
    	        context.response().setStatusCode(500).end(result.cause().getMessage());
    	    }
    	});

    	restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(), 
    			Constants.GET_ACTUATORS, Actuator[].class, resultList);
	}
    
    private void getDeviceLatestValues(RoutingContext context) {
    	Integer deviceId = Integer.parseInt(context.request().getParam("deviceId"));
   
    	Promise<DeviceLatestValuesView[]> resultList = Promise.promise();
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<DeviceLatestValuesView> aux = Stream.of(complete.result())
    					.filter(elem -> elem.getDeviceId() == deviceId)
    					.toList();
    			
                context.response()
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.GET_LATEST_VALUES_VIEW, DeviceLatestValuesView[].class, resultList);
    }
    
    private void getDevicePollutionMap(RoutingContext context) {
    	Integer deviceId = Integer.parseInt(context.request().getParam("deviceId"));
    	
    	Promise<DevicePollutionMap[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<DevicePollutionMap> aux = Arrays.asList(complete.result()).stream()
    					.filter(elem -> elem.getDeviceId() == deviceId)
    					.toList();
    			
                context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.GET_POLLUTION_MAP_VIEW, DevicePollutionMap[].class, resultList);
	}
    
    private void getDeviceHistory(RoutingContext context) {
    	Integer deviceId = Integer.parseInt(context.request().getParam("deviceId"));
    	
    	Promise<DeviceSensorHistory[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<DeviceSensorHistory> aux = Arrays.asList(complete.result()).stream()
    					.filter(elem -> elem.getDeviceId() == deviceId)
    					.toList();
    			
                context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.GET_SENSOR_HISTORY_BY_DEVICE_VIEW, DeviceSensorHistory[].class, resultList);
    }
    
    private void getSensorValues(RoutingContext context) {
    	Integer sensorId = Integer.parseInt(context.request().getParam("sensorId"));
    	
    	Promise<DeviceSensorValue[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<DeviceSensorValue> aux = Arrays.asList(complete.result()).stream()
    					.filter(val -> val.getSensorId() == sensorId)
    					.toList();
    			
                context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.GET_SENSOR_VALUES_VIEW, DeviceSensorValue[].class, resultList);
	}
}