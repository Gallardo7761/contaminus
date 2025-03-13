package net.miarma.contaminus.common;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private final File configFile;
    private final Properties config;

    private ConfigManager() {
		this.configFile = new File(Constants.CONFIG_FILE);
        this.config = new Properties();
        loadConfig();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            config.load(fis);
        } catch (IOException e) {
            Constants.LOGGER.error("Error loading configuration file: ", e);
        }
    }

    public String getJdbcUrl() {
		return String.format("%s://%s:%s/%s",
				config.getProperty("db.protocol"),
				config.getProperty("db.host"),
				config.getProperty("db.port"),
				config.getProperty("db.name"));
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