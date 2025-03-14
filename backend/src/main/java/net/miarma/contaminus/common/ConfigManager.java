package net.miarma.contaminus.common;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();
    private final File configFile;
    private final Properties config;
    private static final String CONFIG_FILE_NAME = "config.properties";

    private ConfigManager() {
    	String path = getBaseDir() + CONFIG_FILE_NAME;
        this.configFile = new File(path);
        this.config = new Properties();
        loadConfig();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            config.load(fis);
        } catch (IOException e) {
            Constants.LOGGER.error("Error loading configuration file: ", e);
        }
    }
    
    public File getConfigFile() {
		return configFile;
	}

    public String getJdbcUrl() {
        return String.format("%s://%s:%s/%s",
                config.getProperty("db.protocol"),
                config.getProperty("db.host"),
                config.getProperty("db.port"),
                config.getProperty("db.name"));
    }
    
    public String getHost() {
        return this.getStringProperty("inet.host");
    }

    public int getDataApiPort() {
        return this.getIntProperty("data-api.port");
    }

    public int getLogicApiPort() {
        return this.getIntProperty("logic-api.port");
    }

    public int getWebserverPort() {
        return this.getIntProperty("web.port");
    }
    
    public String getHomeDir() {
    	return getOS() == OSType.WINDOWS ? 
                "C:/Users/" + System.getProperty("user.name") + "/" :
                System.getProperty("user.home").contains("root") ? "/root/" : 
                "/home/" + System.getProperty("user.name") + "/";
    }
    
    public String getBaseDir() {
		return getHomeDir() + 
				(getOS() == OSType.WINDOWS ? ".contaminus/" :
					getOS() == OSType.LINUX ? ".config/contaminus/" :
				".contaminus/");
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
    
    public String getStringProperty(String key) {
        return config.getProperty(key);
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(config.getProperty(key));
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(config.getProperty(key));
    }

    public void setProperty(String key, String value) {
        config.setProperty(key, value);
        saveConfig();
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "Configuration for: " + Constants.APP_NAME);
        } catch (IOException e) {
            Constants.LOGGER.error("Error saving configuration file: ", e);
        }
    }
}