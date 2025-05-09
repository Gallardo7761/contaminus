package net.miarma.contaminus.verticles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.SingleJsonResponse;
import net.miarma.contaminus.dao.ActuatorDAO;
import net.miarma.contaminus.dao.COValueDAO;
import net.miarma.contaminus.dao.DeviceDAO;
import net.miarma.contaminus.dao.GpsValueDAO;
import net.miarma.contaminus.dao.GroupDAO;
import net.miarma.contaminus.dao.SensorDAO;
import net.miarma.contaminus.dao.WeatherValueDAO;
import net.miarma.contaminus.dao.views.ViewLatestValuesDAO;
import net.miarma.contaminus.dao.views.ViewPollutionMapDAO;
import net.miarma.contaminus.dao.views.ViewSensorHistoryDAO;
import net.miarma.contaminus.dao.views.ViewSensorValueDAO;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.DatabaseProvider;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.Actuator;
import net.miarma.contaminus.entities.Device;
import net.miarma.contaminus.entities.Group;
import net.miarma.contaminus.entities.Sensor;
import net.miarma.contaminus.entities.ViewLatestValues;
import net.miarma.contaminus.entities.ViewPollutionMap;
import net.miarma.contaminus.entities.ViewSensorHistory;
import net.miarma.contaminus.entities.ViewSensorValue;


/*
 * This class is a Verticle that will handle the Data Layer API. 
 */

@SuppressWarnings("unused")
public class DataLayerAPIVerticle extends AbstractVerticle {
    private DatabaseManager dbManager;
    private ConfigManager configManager;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private Pool pool;
    
    private GroupDAO groupDAO;
    private DeviceDAO deviceDAO;
    private SensorDAO sensorDAO;
    private ActuatorDAO actuatorDAO;
    private COValueDAO coValueDAO;
    private WeatherValueDAO weatherValueDAO;
    private GpsValueDAO gpsValueDAO;
    private ViewLatestValuesDAO viewLatestValuesDAO;
    private ViewPollutionMapDAO viewPollutionMapDAO;
    private ViewSensorHistoryDAO viewSensorHistoryDAO;
    private ViewSensorValueDAO viewSensorValueDAO;
    
	public DataLayerAPIVerticle() {
    	this.configManager = ConfigManager.getInstance();		        
    }
    
	@Override
    public void start(Promise<Void> startPromise) {
		Constants.LOGGER.info("📡 Iniciando DataLayerAPIVerticle...");
			
		this.pool = DatabaseProvider.createPool(vertx, configManager);
		this.dbManager = DatabaseManager.getInstance(pool);
        
        this.groupDAO = new GroupDAO(pool);
        this.deviceDAO = new DeviceDAO(pool);
        this.sensorDAO = new SensorDAO(pool);
        this.actuatorDAO = new ActuatorDAO(pool);
        this.coValueDAO = new COValueDAO(pool);
        this.weatherValueDAO = new WeatherValueDAO(pool);
        this.gpsValueDAO = new GpsValueDAO(pool);
        this.viewLatestValuesDAO = new ViewLatestValuesDAO(pool);
        this.viewPollutionMapDAO = new ViewPollutionMapDAO(pool);
        this.viewSensorHistoryDAO = new ViewSensorHistoryDAO(pool);
        this.viewSensorValueDAO = new ViewSensorValueDAO(pool);
		
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
        router.route(HttpMethod.GET, Constants.GROUPS).handler(this::getAllGroups);
        router.route(HttpMethod.GET, Constants.GROUP).handler(this::getGroupById);
        router.route(HttpMethod.POST, Constants.GROUPS).handler(this::addGroup);
        router.route(HttpMethod.PUT, Constants.GROUP).handler(this::updateGroup);

        // Device Routes
        router.route(HttpMethod.GET, Constants.DEVICES).handler(this::getAllDevices);
        router.route(HttpMethod.GET, Constants.DEVICE).handler(this::getDeviceById);
        router.route(HttpMethod.POST, Constants.DEVICES).handler(this::addDevice);
        router.route(HttpMethod.PUT, Constants.DEVICE).handler(this::updateDevice);

        // Sensor Routes
        router.route(HttpMethod.GET, Constants.SENSORS).handler(this::getAllSensors);
        router.route(HttpMethod.GET, Constants.SENSOR).handler(this::getSensorById);
        router.route(HttpMethod.POST, Constants.SENSORS).handler(this::addSensor);
        router.route(HttpMethod.PUT, Constants.SENSOR).handler(this::updateSensor);

        // Actuator Routes
        router.route(HttpMethod.GET, Constants.ACTUATORS).handler(this::getAllActuators);
        router.route(HttpMethod.GET, Constants.ACTUATOR).handler(this::getActuatorById);
        router.route(HttpMethod.POST, Constants.ACTUATORS).handler(this::addActuator);
        router.route(HttpMethod.PUT, Constants.ACTUATOR).handler(this::updateActuator);
    	       
        // Views Routes
        router.route(HttpMethod.GET, Constants.VIEW_LATEST_VALUES).handler(this::getLatestValuesView);
        router.route(HttpMethod.GET, Constants.VIEW_POLLUTION_MAP).handler(this::getDevicePollutionMapView);
        router.route(HttpMethod.GET, Constants.VIEW_SENSOR_VALUES).handler(this::getSensorValuesView);
        router.route(HttpMethod.GET, Constants.VIEW_SENSOR_HISTORY).handler(this::getSensorHistoryByDeviceView);
                
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
		groupDAO.getAll()
			.onSuccess(groups -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(groups));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void getGroupById(RoutingContext context) {
		Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		
		groupDAO.getById(groupId)
			.onSuccess(group -> {
				if (group != null) {
					context.response()
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(gson.toJson(group));
				} else {
					context.response().setStatusCode(404).end();
				}
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void addGroup(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Group group = gson.fromJson(body.toString(), Group.class);
		
		groupDAO.insert(group)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Group added successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void updateGroup(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Group group = gson.fromJson(body.toString(), Group.class);
		
		groupDAO.update(group)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Group updated successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void getAllDevices(RoutingContext context) {
	    Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
	    
	    deviceDAO.getAllByGroupId(groupId)
			.onSuccess(devices -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(devices));
				})
			.onFailure(err -> {
				context.fail(500, err);
				});	
	}

	private void getDeviceById(RoutingContext context) {
	    Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		String deviceId = context.request().getParam("deviceId");
		
		deviceDAO.getByIdAndGroupId(deviceId, groupId)
			.onSuccess(device -> {
				if (device != null) {
					context.response()
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(gson.toJson(device));
				} else {
					context.response().setStatusCode(404).end();
				}
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void addDevice(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Device device = gson.fromJson(body.toString(), Device.class);
		
		deviceDAO.insert(device)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Device added successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void updateDevice(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Device device = gson.fromJson(body.toString(), Device.class);
		
		deviceDAO.update(device)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Device updated successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void getAllSensors(RoutingContext context) {
		Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		String deviceId = context.request().getParam("deviceId");
		
		deviceDAO.getByIdAndGroupId(deviceId, groupId).compose(device -> {
			if (device == null) {
				return Future.succeededFuture(List.of());
			}
			return sensorDAO.getAllByDeviceId(device.getDeviceId());
		}).onSuccess(sensors -> {
			context.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(sensors));
		}).onFailure(err -> {
			context.response().setStatusCode(500).end("Error: " + err.getMessage());
		});
	}
	
	private void getSensorById(RoutingContext context) {
		Integer sensorId = Integer.parseInt(context.request().getParam("sensorId"));
		String deviceId = context.request().getParam("deviceId");
		Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		
		deviceDAO.getByIdAndGroupId(deviceId, groupId).compose(device -> {
			if (device == null) {
				return Future.succeededFuture(null);
			}
			return sensorDAO.getByIdAndDeviceId(sensorId, device.getDeviceId());
		}).onSuccess(sensor -> {
			if (sensor == null) {
				context.response().setStatusCode(404).end("Sensor no encontrado");
				return;
			}
			context.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(sensor));
		}).onFailure(err -> {
			context.response().setStatusCode(500).end("Error: " + err.getMessage());
		});
	}
	
	private void addSensor(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Sensor sensor = gson.fromJson(body.toString(), Sensor.class);
		
		sensorDAO.insert(sensor)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Sensor added successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void updateSensor(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Sensor sensor = gson.fromJson(body.toString(), Sensor.class);
		
		sensorDAO.update(sensor)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Sensor updated successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void getAllActuators(RoutingContext context) {
		Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		String deviceId = context.request().getParam("deviceId");
		
		deviceDAO.getByIdAndGroupId(deviceId, groupId).compose(device -> {
			if (device == null) {
				return Future.succeededFuture(List.of());
			}
			return actuatorDAO.getAllByDeviceId(device.getDeviceId());
		}).onSuccess(actuators -> {
			context.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(actuators));
		}).onFailure(err -> {
			context.response().setStatusCode(500).end("Error: " + err.getMessage());
		});
	}
	
	private void getActuatorById(RoutingContext context) {
		Integer groupId = Integer.parseInt(context.request().getParam("groupId"));
		String deviceId = context.request().getParam("deviceId");	
		Integer actuatorId = Integer.parseInt(context.request().getParam("actuatorId"));
		
		deviceDAO.getByIdAndGroupId(deviceId, groupId).compose(device -> {
			if (device == null) {
				return Future.succeededFuture(null);
			}
			return actuatorDAO.getByIdAndDeviceId(actuatorId, device.getDeviceId());
		}).onSuccess(actuator -> {
			if (actuator == null) {
				context.response().setStatusCode(404).end("Actuator no encontrado");
				return;
			}
			context.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(actuator));
		}).onFailure(err -> {
			context.response().setStatusCode(500).end("Error: " + err.getMessage());
		});
	}
	
	private void addActuator(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Actuator actuator = gson.fromJson(body.toString(), Actuator.class);
		
		actuatorDAO.insert(actuator)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Actuator added successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
	
	private void updateActuator(RoutingContext context) {
		JsonObject body = context.body().asJsonObject();
		Actuator actuator = gson.fromJson(body.toString(), Actuator.class);
		
		actuatorDAO.update(actuator)
			.onSuccess(result -> {
				context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(SingleJsonResponse.of("Actuator updated successfully")));
			})
			.onFailure(err -> {
				context.fail(500, err);
			});
	}
		
	private void getLatestValuesView(RoutingContext context) {
		String query = QueryBuilder
			.select(ViewLatestValues.class)
			.build();
		
		dbManager.execute(query, ViewLatestValues.class,
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
			.select(ViewPollutionMap.class)
			.build();
		
		dbManager.execute(query, ViewPollutionMap.class,
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
			.select(ViewSensorValue.class)
			.build();
		
		dbManager.execute(query, ViewSensorValue.class,
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
			.select(ViewSensorHistory.class)
			.build();
		
		dbManager.execute(query, ViewSensorHistory.class,
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