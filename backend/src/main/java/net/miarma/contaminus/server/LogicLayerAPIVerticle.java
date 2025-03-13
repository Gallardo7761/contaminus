package net.miarma.contaminus.server;

import java.util.Arrays;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.DatabaseManager;
import net.miarma.contaminus.util.SystemUtil;

@SuppressWarnings("unused")
public class LogicLayerAPIVerticle extends AbstractVerticle {
	private DatabaseManager dbManager;
    private Gson gson;
    private ConfigManager configManager;
	
    @Override
    public void start(Promise<Void> startPromise) {
        Constants.LOGGER.info("📡 Iniciando LogicApiVerticle...");

    	configManager = ConfigManager.getInstance();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		dbManager = DatabaseManager.getInstance();
		
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
	        .listen(
                SystemUtil.getLogicApiPort(),
                SystemUtil.getHost(),
                result -> {
	                if (result.succeeded()) {
	                    Constants.LOGGER.info(String.format(
	                        "🟢 ApiVerticle desplegado. (http://%s:%d)", SystemUtil.getHost(), SystemUtil.getLogicApiPort()
	                    ));
	                    startPromise.complete();
	                } else {
	                    Constants.LOGGER.error("🔴 Error al desplegar LogicApiVerticle", result.cause());
	                    startPromise.fail(result.cause());
	                }
            });
    }
       
    
    private void getGroupDevices(RoutingContext context) {
    	context.response().end("TODO");
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