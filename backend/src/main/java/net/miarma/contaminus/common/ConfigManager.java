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
        
        if (!configFile.exists()) {
            try {
                createFiles();
            } catch (IOException e) {
                Constants.LOGGER.error("Error creating configuration files: ", e);
            }
        }

        loadConfig();
    }
    
    public static void init() {
		ConfigManager.getInstance();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void createFiles() throws IOException {
        File baseDir = new File(Constants.BASE_DIR);
        if (!baseDir.exists()) baseDir.mkdirs();

        try (InputStream defaultConfigStream = getClass().getClassLoader().getResourceAsStream("default.properties");
             FileOutputStream fos = new FileOutputStream(configFile)) {

            if (defaultConfigStream != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = defaultConfigStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } else {
                Constants.LOGGER.error("File not found: default.properties");
            }
        }
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            config.load(fis);
        } catch (IOException e) {
            Constants.LOGGER.error("Error loading configuration file: ", e);
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