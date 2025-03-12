package net.miarma.contaminus.common;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class Constants {
	public static final String APP_NAME = "ContaminUS";
	public static final String API_PREFIX = "/api/v1";
	public static final String HOME_DIR = SystemInfo.getOS() == OSType.WINDOWS ? 
			"C:/Users/" + System.getProperty("user.name") + "/" :
				System.getProperty("user.home") + "/";
	public static final String BASE_DIR = HOME_DIR + 
			(SystemInfo.getOS() == OSType.WINDOWS ? ".contaminus" :
			 SystemInfo.getOS() == OSType.LINUX ? ".config" + "/" + 
				 "contaminus" : null);
	public static final String CONFIG_FILE = BASE_DIR + "/" + "config.properties";
	public static final Logger LOGGER = LoggerFactory.getLogger(APP_NAME);
	
	public static final String GET_GROUPS = API_PREFIX + "/groups";
	public static final String GET_GROUP_BY_ID = API_PREFIX + "/groups/:groupId";
	public static final String GET_GROUP_DEVICES = API_PREFIX + "/groups/:groupId/devices";
	public static final String POST_GROUPS = API_PREFIX + "/groups";
	public static final String PUT_GROUP_BY_ID = API_PREFIX + "/groups/:groupId";

	public static final String GET_DEVICES = API_PREFIX + "/devices";
	public static final String GET_DEVICE_BY_ID = API_PREFIX + "/devices/:deviceId";
	public static final String GET_DEVICE_SENSORS = API_PREFIX + "/devices/:deviceId/sensors";
	public static final String POST_DEVICES = API_PREFIX + "/devices";
	public static final String PUT_DEVICE_BY_ID = API_PREFIX + "/devices/:deviceId";
	public static final String GET_DEVICE_ACTUATORS = API_PREFIX + "/devices/:deviceId/actuators";
	public static final String GET_DEVICE_LATEST_VALUES = API_PREFIX + "/devices/:deviceId/latest";
	public static final String GET_DEVICE_POLLUTION_MAP = API_PREFIX + "/devices/:deviceId/pollution-map";
	public static final String GET_DEVICE_HISTORY = API_PREFIX + "/devices/:deviceId/history";
	
	public static final String GET_SENSORS = API_PREFIX + "/sensors";
	public static final String GET_SENSOR_BY_ID = API_PREFIX + "/sensors/:sensorId";
	public static final String GET_SENSOR_VALUES = API_PREFIX + "/sensors/:sensorId/values";
	public static final String POST_SENSORS = API_PREFIX + "/sensors";
	public static final String PUT_SENSOR_BY_ID = API_PREFIX + "/sensors/:sensorId";

	public static final String GET_ACTUATORS = API_PREFIX + "/actuators";
	public static final String GET_ACTUATOR_BY_ID = API_PREFIX + "/actuators/:actuatorId";
	public static final String POST_ACTUATORS = API_PREFIX + "/actuators";
	public static final String PUT_ACTUATOR_BY_ID = API_PREFIX + "/actuators/:actuatorId";
	
	
	private Constants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }
}
