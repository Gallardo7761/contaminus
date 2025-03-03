package net.miarma.contaminus.common;

import java.io.File;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class Constants {
	public static final String APP_NAME = "ContaminUS";
	public static final String API_PREFIX = "/api/v1";
	public static final String HOME_DIR = System.getProperty("user.home") + File.separator;
	public static final String BASE_DIR = HOME_DIR + 
			(SystemInfo.getOS() == OSType.WINDOWS ? ".contaminus" :
			 SystemInfo.getOS() == OSType.LINUX ? ".config" + File.separator + 
				 "contaminus" : null);
	public static final String CONFIG_FILE = BASE_DIR + File.separator + "config.properties";
	public static final Logger LOGGER = LoggerFactory.getLogger(APP_NAME);
	
	private Constants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }
}
