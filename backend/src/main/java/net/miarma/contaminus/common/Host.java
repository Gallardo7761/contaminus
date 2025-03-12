package net.miarma.contaminus.common;

public class Host {
	static ConfigManager configManager = ConfigManager.getInstance();
	static String host = configManager.getStringProperty("inet.host");
	static String origin = configManager.getStringProperty("inet.origin");
	static int apiPort = configManager.getIntProperty("api.port");
	static int webserverPort = configManager.getIntProperty("webserver.port");
	
	public static String getHost() {
		return host;
	}
	
	public static int getApiPort() {
		return apiPort;
	}
	
	public static int getWebserverPort() {
		return webserverPort;
	}
}
