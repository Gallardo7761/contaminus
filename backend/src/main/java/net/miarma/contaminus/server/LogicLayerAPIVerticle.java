package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.entities.Device;
import net.miarma.contaminus.util.RestClientUtil;

public class LogicLayerAPIVerticle extends AbstractVerticle {
    private ConfigManager configManager;
    private final Gson gson = new Gson();
    private RestClientUtil restClient;

    public LogicLayerAPIVerticle() {
    	this.configManager = ConfigManager.getInstance();
    	WebClientOptions options = new WebClientOptions()
    			.setUserAgent("ContaminUS");
    	this.restClient = new RestClientUtil(WebClient.create(vertx, options));
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
        
        // Group Routes
        router.route(HttpMethod.GET, Constants.GET_GROUP_DEVICES).handler(this::getGroupDevices);

        // Device Routes
        router.route(HttpMethod.GET, Constants.GET_DEVICE_SENSORS).handler(this::getDeviceSensors);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_ACTUATORS).handler(this::getDeviceActuators);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_LATEST_VALUES).handler(this::getDeviceLatestValues);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_POLLUTION_MAP).handler(this::getDevicePollutionMap);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_HISTORY).handler(this::getDeviceHistory);

        // Sensor Routes
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
                List<Device> aux = Arrays.asList(complete.result()).stream()
                        .filter(d -> d.getGroupId() == groupId)
                        .toList();
                context.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(gson.toJson(aux));
            } else {
                context.fail(500, complete.cause());
            }
        });
        
        this.restClient.getRequest(configManager.getDataApiPort(), configManager.getHost(),
                Constants.GET_GROUP_DEVICES, Device[].class, resultList);
    }
    
    private void getDeviceSensors(RoutingContext context) {
    	context.response().end("TODO");
    }
    
    private void getDeviceActuators(RoutingContext context) {
    	context.response().end("TODO");
	}
    
    private void getDeviceLatestValues(RoutingContext context) {
    	context.response().end("TODO");
    }
    
    private void getDevicePollutionMap(RoutingContext context) {
    	context.response().end("TODO");
	}
    
    private void getDeviceHistory(RoutingContext context) {
    	context.response().end("TODO");
    }
    
    private void getSensorValues(RoutingContext context) {
    	context.response().end("TODO");
	}
      
}