package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
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
import net.miarma.contaminus.database.entities.DeviceLatestValuesView;
import net.miarma.contaminus.database.entities.DevicePollutionMap;
import net.miarma.contaminus.database.entities.DeviceSensorHistory;
import net.miarma.contaminus.database.entities.DeviceSensorValue;
import net.miarma.contaminus.database.entities.Sensor;

/*
 * This class is a Verticle that will handle the Data Layer API. 
 */

@SuppressWarnings("unused")
public class DataLayerAPIVerticle extends AbstractVerticle {
	private JDBCPool pool;
    private DatabaseManager dbManager;
    private ConfigManager configManager;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    
    @SuppressWarnings("deprecation")
	public DataLayerAPIVerticle() {
    	this.configManager = ConfigManager.getInstance();
    	String jdbcUrl = configManager.getJdbcUrl();
        String dbUser = configManager.getStringProperty("db.user");
        String dbPwd = configManager.getStringProperty("db.pwd");
        Integer poolSize = configManager.getIntProperty("db.poolSize");

        JsonObject dbConfig = new JsonObject()
                .put("url", jdbcUrl)
                .put("user", dbUser)
                .put("password", dbPwd)
                .put("max_pool_size", poolSize != null ? poolSize : 10);
		        
        this.pool = JDBCPool.pool(Vertx.vertx(), dbConfig);
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
    	       
        // Views Routes
        router.route(HttpMethod.GET, Constants.GET_LATEST_VALUES_VIEW).handler(this::getLatestValuesView);
        router.route(HttpMethod.GET, Constants.GET_POLLUTION_MAP_VIEW).handler(this::getDevicePollutionMapView);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_VALUES_VIEW).handler(this::getSensorValuesView);
        router.route(HttpMethod.GET, Constants.GET_SENSOR_HISTORY_BY_DEVICE_VIEW).handler(this::getSensorHistoryByDeviceView);
        
        vertx.createHttpServer()
	        .requestHandler(router)
	        .listen(configManager.getDataApiPort(), configManager.getHost());
    
        pool.query("SELECT 1").execute(ar -> {
            if (ar.succeeded()) {
                Constants.LOGGER.info("🟢 Connected to DB");
                startPromise.complete();
            } else {
            	Constants.LOGGER.error("🔴 Failed to connect to DB: " + ar.cause());
                startPromise.fail(ar.cause());
            }
        });   
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
		
	private void getLatestValuesView(RoutingContext context) {
		String query = QueryBuilder
			.select(DeviceLatestValuesView.class)
			.build();
		
		dbManager.execute(query, DeviceLatestValuesView.class,
			onSuccess -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(onSuccess));
			},
			onFailure -> {
				context.fail(500, onFailure);
			});
	}
	
	private void getDevicePollutionMapView(RoutingContext context) {	
		String query = QueryBuilder
			.select(DevicePollutionMap.class)
			.build();
		
		dbManager.execute(query, DevicePollutionMap.class,
			onSuccess -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(onSuccess));
			},
			onFailure -> {
				context.fail(500, onFailure);
			});
	}
	
	private void getSensorValuesView(RoutingContext context) {		
		String query = QueryBuilder
			.select(DeviceSensorValue.class)
			.build();
		
		dbManager.execute(query, DeviceSensorValue.class,
			onSuccess -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(onSuccess));
			},
			onFailure -> {
				context.fail(500, onFailure);
			});
	}
	
	private void getSensorHistoryByDeviceView(RoutingContext context) {		
		String query = QueryBuilder
			.select(DeviceSensorHistory.class)
			.build();
		
		dbManager.execute(query, DeviceSensorHistory.class,
			onSuccess -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(onSuccess));
			},
			onFailure -> {
				context.fail(500, onFailure);
			});
	}
    
}
