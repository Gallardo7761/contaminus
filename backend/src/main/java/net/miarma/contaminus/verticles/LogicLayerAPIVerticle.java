package net.miarma.contaminus.verticles;

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
import net.miarma.contaminus.entities.ViewLatestValues;
import net.miarma.contaminus.entities.ViewPollutionMap;
import net.miarma.contaminus.entities.ViewSensorHistory;
import net.miarma.contaminus.entities.ViewSensorValue;
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
	    		Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.OPTIONS));
		Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));

        router.route().handler(CorsHandler.create()
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));
        
        router.route().handler(BodyHandler.create());
        
        router.route(HttpMethod.GET, Constants.LATEST_VALUES).handler(this::getDeviceLatestValues);
        router.route(HttpMethod.GET, Constants.POLLUTION_MAP).handler(this::getDevicePollutionMap);
        router.route(HttpMethod.GET, Constants.HISTORY).handler(this::getDeviceHistory);
        router.route(HttpMethod.GET, Constants.SENSOR_VALUES).handler(this::getSensorValues);

        vertx.createHttpServer()
	        .requestHandler(router)
	        .listen(configManager.getLogicApiPort(), configManager.getHost());
        
        startPromise.complete();
    }
           
    private void getDeviceLatestValues(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
   
    	Promise<ViewLatestValues[]> resultList = Promise.promise();
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<ViewLatestValues> aux = Stream.of(complete.result())
    					.filter(elem -> deviceId.equals(elem.getDeviceId()))
    					.toList();
    			
                context.response()
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.VIEW_LATEST_VALUES, ViewLatestValues[].class, resultList);
    }
    
    private void getDevicePollutionMap(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
    	
    	Promise<ViewPollutionMap[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<ViewPollutionMap> aux = Arrays.asList(complete.result()).stream()
    					.filter(elem -> deviceId.equals(elem.getDeviceId()))
    					.toList();
    			
                context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.VIEW_POLLUTION_MAP, ViewPollutionMap[].class, resultList);
	}
    
    private void getDeviceHistory(RoutingContext context) {
    	String deviceId = context.request().getParam("deviceId");
    	
    	Promise<ViewSensorHistory[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<ViewSensorHistory> aux = Arrays.asList(complete.result()).stream()
    					.filter(elem -> deviceId.equals(elem.getDeviceId()))
    					.toList();
    			
                context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(gson.toJson(aux));
    		} else {
    			context.fail(500, complete.cause());
    		}
    	});
    	
    	this.restClient.getRequest(configManager.getDataApiPort(), "http://" + configManager.getHost(),
    			Constants.VIEW_SENSOR_HISTORY, ViewSensorHistory[].class, resultList);
    }
    
    private void getSensorValues(RoutingContext context) {
    	Integer sensorId = Integer.parseInt(context.request().getParam("sensorId"));
    	
    	Promise<ViewSensorValue[]> resultList = Promise.promise();
    	
    	resultList.future().onComplete(complete -> {
    		if (complete.succeeded()) {
    			List<ViewSensorValue> aux = Arrays.asList(complete.result()).stream()
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
    			Constants.VIEW_SENSOR_VALUES, ViewSensorValue[].class, resultList);
	}
}