package net.miarma.contaminus.util;

import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.OSType;

public class SystemUtil {
	static ConfigManager configManager = ConfigManager.getInstance();
	static String host = configManager.getStringProperty("inet.host");
	static String origin = configManager.getStringProperty("inet.origin");
	static int dataApiPort = 8081;
	static int logicApiPort = 8082;
	static int webserverPort = 8080;
	
	public static String getHost() {
		return host;
	}
	
	public static int getDataApiPort() {
		return dataApiPort;
	}
	
	public static int getLogicApiPort() {
		return logicApiPort;
	}
	
	public static int getWebserverPort() {
		return webserverPort;
	}
	
	public static OSType getOS() {
	    String os = System.getProperty("os.name").toLowerCase();
	    if (os.contains("win")) {
	        return OSType.WINDOWS;
	    } else if (os.contains("nix") || os.contains("nux")) {
	        return OSType.LINUX;
	    } else {
	        return OSType.INVALID_OS;
	    }
	}

}
