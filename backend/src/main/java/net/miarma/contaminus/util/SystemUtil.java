package net.miarma.contaminus.util;

import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.OSType;

public class SystemUtil {
    private static ConfigManager configManager;
    
    private static String host;
    private static int dataApiPort;
    private static int logicApiPort;
    private static int webserverPort;

    public static void init() {
        configManager = ConfigManager.getInstance();
        
        host = configManager.getStringProperty("inet.host");
        dataApiPort = configManager.getIntProperty("data-api.port");
        logicApiPort = configManager.getIntProperty("logic-api.port");
        webserverPort = configManager.getIntProperty("web.port");
    }

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
