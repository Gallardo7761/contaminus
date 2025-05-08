package net.miarma.contaminus.common;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class Constants {
	public static final String APP_NAME = "ContaminUS";
	public static final String API_PREFIX = "/api/v1";
	public static final String RAW_API_PREFIX = "/api/raw/v1";
    public static Logger LOGGER = LoggerFactory.getLogger(Constants.APP_NAME);

	
	/* API Endpoints */
    public static final String POST_PAYLOAD = RAW_API_PREFIX + "/device-payload";
    
	public static final String GET_GROUPS = RAW_API_PREFIX + "/groups";
	public static final String POST_GROUPS = RAW_API_PREFIX + "/groups";
	public static final String PUT_GROUP_BY_ID = RAW_API_PREFIX + "/groups/:groupId";

	public static final String GET_DEVICES = RAW_API_PREFIX + "/devices";
	public static final String POST_DEVICES = RAW_API_PREFIX + "/devices";
	public static final String PUT_DEVICE_BY_ID = RAW_API_PREFIX + "/devices/:deviceId";
	
	public static final String GET_SENSORS = RAW_API_PREFIX + "/sensors";
	public static final String POST_SENSORS = RAW_API_PREFIX + "/sensors";
	public static final String PUT_SENSOR_BY_ID = RAW_API_PREFIX + "/sensors/:sensorId";

	public static final String GET_ACTUATORS = RAW_API_PREFIX + "/actuators";
	public static final String POST_ACTUATORS = RAW_API_PREFIX + "/actuators";
	public static final String PUT_ACTUATOR_BY_ID = RAW_API_PREFIX + "/actuators/:actuatorId";
	
	public static final String GET_CO_BY_DEVICE_VIEW = RAW_API_PREFIX + "/v_co_by_device";

	public static final String GET_GPS_BY_DEVICE_VIEW = RAW_API_PREFIX + "/v_gps_by_device";
	public static final String GET_LATEST_VALUES_VIEW = RAW_API_PREFIX + "/v_latest_values";
	public static final String GET_POLLUTION_MAP_VIEW = RAW_API_PREFIX + "/v_pollution_map";
	public static final String GET_SENSOR_HISTORY_BY_DEVICE_VIEW = RAW_API_PREFIX + "/v_sensor_history_by_device";
	public static final String GET_SENSOR_VALUES_VIEW = RAW_API_PREFIX + "/v_sensor_values";
	public static final String GET_WEATHER_BY_DEVICE_VIEW = RAW_API_PREFIX + "/v_weather_by_device";
	
	/* Bussiness Logic API */
	public static final String GET_GROUP_BY_ID = API_PREFIX + "/groups/:groupId";
	public static final String GET_GROUP_DEVICES = API_PREFIX + "/groups/:groupId/devices";
	public static final String GET_DEVICE_BY_ID = API_PREFIX + "/devices/:deviceId";
	public static final String GET_DEVICE_SENSORS = API_PREFIX + "/devices/:deviceId/sensors";
	public static final String GET_DEVICE_ACTUATORS = API_PREFIX + "/devices/:deviceId/actuators";
	public static final String GET_DEVICE_LATEST_VALUES = API_PREFIX + "/devices/:deviceId/latest";
	public static final String GET_DEVICE_POLLUTION_MAP = API_PREFIX + "/devices/:deviceId/pollution-map";
	public static final String GET_DEVICE_HISTORY = API_PREFIX + "/devices/:deviceId/history";
	public static final String GET_SENSOR_BY_ID = API_PREFIX + "/sensors/:sensorId";
	public static final String GET_SENSOR_VALUES = API_PREFIX + "/sensors/:sensorId/values";
	public static final String GET_ACTUATOR_BY_ID = API_PREFIX + "/actuators/:actuatorId";
	
	private Constants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }
}
