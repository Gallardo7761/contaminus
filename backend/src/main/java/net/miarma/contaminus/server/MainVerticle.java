package net.miarma.contaminus.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;

public class MainVerticle extends AbstractVerticle {
    private ConfigManager configManager;
    private JDBCPool pool;

    public static void main(String[] args) {    	
    	Launcher.executeCommand("run", MainVerticle.class.getName());
    }
	
	@SuppressWarnings("deprecation")
	private void init() {	
		configManager = ConfigManager.getInstance();
		
		String jdbcUrl = configManager.getJdbcUrl();
        String dbUser = configManager.getStringProperty("db.user");
        String dbPwd = configManager.getStringProperty("db.pwd");
        Integer poolSize = configManager.getIntProperty("db.poolSize");

        JsonObject dbConfig = new JsonObject()
                .put("url", jdbcUrl)
                .put("user", dbUser)
                .put("password", dbPwd)
                .put("max_pool_size", poolSize != null ? poolSize : 10);
		
        pool = JDBCPool.pool(vertx, dbConfig);
        
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
        	pool.query("SELECT 1").execute(ar -> {
                if (ar.succeeded()) {
                    Constants.LOGGER.info("🟢 Connected to DB");
                    deployVerticles(startPromise);
                } else {
                	Constants.LOGGER.error("🔴 Failed to connect to DB: " + ar.cause());
                    startPromise.fail(ar.cause());
                }
            });
    	} catch (Exception e) {
			System.err.println("🔴 Error starting the application: " + e);
			startPromise.fail(e);
		}
    }
    
    private void deployVerticles(Promise<Void> startPromise) {
        final DeploymentOptions options = new DeploymentOptions();
        options.setThreadingModel(ThreadingModel.WORKER);

        vertx.deployVerticle(new DataLayerAPIVerticle(pool), options, result -> { // Pasa el pool
            if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 DatabaseVerticle desplegado");
            } else {
            	Constants.LOGGER.error("🔴 Error deploying DataLayerAPIVerticle: " + result.cause());
            }
        });

        vertx.deployVerticle(new LogicLayerAPIVerticle(), options, result -> {
        	if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 LogicLayerAPIVerticle desplegado");
            } else {
            	Constants.LOGGER.error("🔴 Error deploying LogicLayerAPIVerticle: " + result.cause());
            }
        });

        vertx.deployVerticle(new WebServerVerticle(), result -> {
        	if (result.succeeded()) {
            	Constants.LOGGER.info("🟢 WebServerVerticle desplegado");
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
