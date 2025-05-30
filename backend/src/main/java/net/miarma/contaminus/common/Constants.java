package net.miarma.contaminus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String APP_NAME = "ContaminUS";
	public static final String API_PREFIX = "/api/v1";
	public static final String RAW_API_PREFIX = "/api/raw/v1";
	public static final String CONTAMINUS_EB = "contaminus.eventbus";
    public static Logger LOGGER = LoggerFactory.getLogger(Constants.APP_NAME);
	
    public static final Integer SENSOR_ROLE = 0;
    public static final Integer ACTUATOR_ROLE = 1;
    
	/* API Endpoints */    
	public static final String GROUPS = RAW_API_PREFIX + "/groups"; // GET, POST
	public static final String GROUP = RAW_API_PREFIX + "/groups/:groupId"; // GET, PUT
	
	public static final String DEVICES = RAW_API_PREFIX + "/groups/:groupId/devices"; // GET, POST
	public static final String DEVICE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId"; // GET, PUT
	public static final String LATEST_VALUES = API_PREFIX + "/groups/:groupId/devices/:deviceId/latest-values"; // GET
	public static final String POLLUTION_MAP = API_PREFIX + "/groups/:groupId/devices/:deviceId/pollution-map"; // GET
	public static final String HISTORY = API_PREFIX + "/groups/:groupId/devices/:deviceId/history"; // GET
	
	public static final String SENSORS = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors"; // GET, POST
	public static final String SENSOR = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId"; // GET, PUT
	public static final String SENSOR_VALUES = API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/values"; // GET
	
	public static final String BATCH = API_PREFIX + "/batch"; // POST
	public static final String ADD_GPS_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/gps_values"; // POST
	public static final String ADD_WEATHER_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/weather_values"; // POST
	public static final String ADD_CO_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/co_values"; // POST
	
	public static final String ACTUATORS = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators"; // GET, POST
	public static final String ACTUATOR = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators/:actuatorId"; // GET, PUT
	public static final String ACTUATOR_STATUS = API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators/:actuatorId/status"; // GET, PUT
		
	public static final String VIEW_LATEST_VALUES = RAW_API_PREFIX + "/v_latest_values"; // GET
	public static final String VIEW_POLLUTION_MAP = RAW_API_PREFIX + "/v_pollution_map"; // GET
	public static final String VIEW_SENSOR_HISTORY = RAW_API_PREFIX + "/v_sensor_history_by_device"; // GET
	public static final String VIEW_SENSOR_VALUES = RAW_API_PREFIX + "/v_sensor_values"; // GET
	
	private Constants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }
}
