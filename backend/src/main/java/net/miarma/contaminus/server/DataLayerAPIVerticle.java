package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.jdbcclient.JDBCPool;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.DatabaseManager;
import net.miarma.contaminus.database.QueryBuilder;
import net.miarma.contaminus.database.entities.Device;
import net.miarma.contaminus.database.entities.Sensor;

@SuppressWarnings("unused")
public class DataLayerAPIVerticle extends AbstractVerticle {
	private JDBCPool pool;
    private DatabaseManager dbManager;
    private ConfigManager configManager;
    private Gson gson = new Gson();
    
    public DataLayerAPIVerticle(JDBCPool pool) {
        this.pool = pool;
        this.configManager = ConfigManager.getInstance();
    }
    
	@Override
    public void start(Promise<Void> startPromise) {
		Constants.LOGGER.info("📡 Iniciando DataLayerAPIVerticle...");
		dbManager = DatabaseManager.getInstance(pool);
		
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
        router.route(HttpMethod.GET, Constants.GET_GROUPS).handler(this::getAllGroups);
        router.route(HttpMethod.GET, Constants.GET_GROUP_BY_ID).handler(this::getGroupById);
        router.route(HttpMethod.POST, Constants.POST_GROUPS).handler(this::addGroup);
        router.route(HttpMethod.PUT, Constants.PUT_GROUP_BY_ID).handler(this::updateGroup);

        // Device Routes
        router.route(HttpMethod.GET, Constants.GET_DEVICES).handler(this::getAllDevices);
        router.route(HttpMethod.GET, Constants.GET_DEVICE_BY_ID).handler(this::getDeviceById);
        router.route(HttpMethod.POST, Constants.POST_DEVICES).handler(this::addDevice);
        router.route(HttpMethod.PUT, Constants.PUT_DEVICE_BY_ID).handler(this::updateDevice);

        // Sensor Routes
        router.route(HttpMethod.GET, Constants.GET_SENSORS).handler(this::getAllSensors);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_BY_ID).handler(this::getSensorById);
        router.route(HttpMethod.POST, Constants.POST_SENSORS).handler(this::addSensor);
        router.route(HttpMethod.PUT, Constants.PUT_SENSOR_BY_ID).handler(this::updateSensor);

        // Actuator Routes
        router.route(HttpMethod.GET, Constants.GET_ACTUATORS).handler(this::getAllActuators);
        router.route(HttpMethod.GET, Constants.GET_ACTUATOR_BY_ID).handler(this::getActuatorById);
        router.route(HttpMethod.POST, Constants.POST_ACTUATORS).handler(this::addActuator);
        router.route(HttpMethod.PUT, Constants.PUT_ACTUATOR_BY_ID).handler(this::updateActuator);
    	       
        vertx.createHttpServer()
	        .requestHandler(router)
	        .listen(configManager.getDataApiPort(), configManager.getHost());
    
        startPromise.complete();
    }

	private void getAllGroups(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getGroupById(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void addGroup(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void updateGroup(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getAllDevices(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getDeviceById(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void addDevice(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void updateDevice(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getAllSensors(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getSensorById(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void addSensor(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void updateSensor(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getAllActuators(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void getActuatorById(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void addActuator(RoutingContext context) {
		context.response().end("TODO");
	}
	
	private void updateActuator(RoutingContext context) {
		context.response().end("TODO");
	}
    
}
