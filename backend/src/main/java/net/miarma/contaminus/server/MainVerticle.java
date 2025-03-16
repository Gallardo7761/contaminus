package net.miarma.contaminus.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;

public class MainVerticle extends AbstractVerticle {
    private ConfigManager configManager;

    public static void main(String[] args) {    	
    	Launcher.executeCommand("run", MainVerticle.class.getName());
    }
	
	private void init() {	
		this.configManager = ConfigManager.getInstance();
	    initializeDirectories();
	    copyDefaultConfig();
    }
    
    private void initializeDirectories() {        
        File baseDir = new File(this.configManager.getBaseDir());
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }
    
    private void copyDefaultConfig() {
        File configFile = new File(configManager.getConfigFile().getAbsolutePath());
        if (!configFile.exists()) {
            try (InputStream in = MainVerticle.class.getClassLoader().getResourceAsStream("default.properties")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Constants.LOGGER.error("🔴 Default config file not found in resources");
                }
            } catch (IOException e) {
                Constants.LOGGER.error("🔴 Failed to copy default config file", e);
            }
        }
    }
    
    @Override
    public void start(Promise<Void> startPromise) {   
    	try {
    		System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        	init();
        	deployVerticles(startPromise);
    	} catch (Exception e) {
			Constants.LOGGER.error("🔴 Error starting the application: " + e);
			startPromise.fail(e);
		}
    }
    
    private void deployVerticles(Promise<Void> startPromise) {
        final DeploymentOptions options = new DeploymentOptions();
        options.setThreadingModel(ThreadingModel.WORKER);

        vertx.deployVerticle(new DataLayerAPIVerticle(), options, result -> {
            if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 DatabaseVerticle desplegado");
            	Constants.LOGGER.info("\t🔗 API URL: " + configManager.getHost() 
            		+ ":" + configManager.getDataApiPort());
            } else {
            	Constants.LOGGER.error("🔴 Error deploying DataLayerAPIVerticle: " + result.cause());
            }
        });

        vertx.deployVerticle(new LogicLayerAPIVerticle(), options, result -> {
        	if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 LogicLayerAPIVerticle desplegado");
            	Constants.LOGGER.info("\t🔗 API URL: " + configManager.getHost() 
            		+ ":" +  configManager.getLogicApiPort());
            } else {
            	Constants.LOGGER.error("🔴 Error deploying LogicLayerAPIVerticle: " + result.cause());
            }
        });

        vertx.deployVerticle(new WebServerVerticle(), result -> {
        	if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 WebServerVerticle desplegado");
            	Constants.LOGGER.info("\t🔗 WEB SERVER URL: " + configManager.getHost() 
            		+ ":" +  configManager.getWebserverPort());
            } else {
            	Constants.LOGGER.error("🔴 Error deploying WebServerVerticle: " + result.cause());
            }
        });

        startPromise.complete();
    }
    
    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        getVertx().deploymentIDs()
            .forEach(v -> getVertx().undeploy(v));
    }

}
