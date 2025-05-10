package net.miarma.contaminus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String APP_NAME = "ContaminUS";
	public static final String API_PREFIX = "/api/v1";
	public static final String RAW_API_PREFIX = "/api/raw/v1";
	public static final String CONTAMINUS_EB = "contaminus.eventbus";
    public static Logger LOGGER = LoggerFactory.getLogger(Constants.APP_NAME);
	
	/* API Endpoints */    
	public static final String GROUPS = RAW_API_PREFIX + "/groups";
	public static final String GROUP = RAW_API_PREFIX + "/groups/:groupId";
	
	public static final String DEVICES = RAW_API_PREFIX + "/groups/:groupId/devices";
	public static final String DEVICE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId";
	public static final String LATEST_VALUES = API_PREFIX + "/groups/:groupId/devices/:deviceId/latest-values";
	public static final String POLLUTION_MAP = API_PREFIX + "/groups/:groupId/devices/:deviceId/pollution-map";
	public static final String HISTORY = API_PREFIX + "/groups/:groupId/devices/:deviceId/history";
	
	public static final String SENSORS = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors";
	public static final String SENSOR = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId";
	public static final String SENSOR_VALUES = API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/values";
	
	public static final String BATCH = API_PREFIX + "/batch";
	public static final String ADD_GPS_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/gps_values";
	public static final String ADD_WEATHER_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/weather_values";
	public static final String ADD_CO_VALUE = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/sensors/:sensorId/co_values";
	
	public static final String ACTUATORS = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators";
	public static final String ACTUATOR = RAW_API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators/:actuator_id";
	public static final String ACTUATOR_STATUS = API_PREFIX + "/groups/:groupId/devices/:deviceId/actuators/:actuator_id/status";
		
	public static final String VIEW_LATEST_VALUES = RAW_API_PREFIX + "/v_latest_values";
	public static final String VIEW_POLLUTION_MAP = RAW_API_PREFIX + "/v_pollution_map";
	public static final String VIEW_SENSOR_HISTORY = RAW_API_PREFIX + "/v_sensor_history_by_device";
	public static final String VIEW_SENSOR_VALUES = RAW_API_PREFIX + "/v_sensor_values";
	
	private Constants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }
}
