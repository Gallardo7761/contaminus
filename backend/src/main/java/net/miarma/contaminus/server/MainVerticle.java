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
import io.vertx.core.impl.logging.LoggerFactory;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.OSType;
import net.miarma.contaminus.util.SystemUtil;

public class MainVerticle extends AbstractVerticle {
    
    @Override
    public void start(Promise<Void> startPromise) {        
        final DeploymentOptions options = new DeploymentOptions();
        options.setThreadingModel(ThreadingModel.WORKER);
        
        getVertx().deployVerticle(new DataLayerAPIVerticle(), options);
        getVertx().deployVerticle(new LogicLayerAPIVerticle(), options);
        getVertx().deployVerticle(new HttpServerVerticle());
    }
    
    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        getVertx().deploymentIDs()
            .forEach(v -> getVertx().undeploy(v));
    }
    
    public static void main(String[] args) {
    	initializeConstants();
    	initializeDirectories();
    	copyDefaultConfig();
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }
    
    private static void initializeConstants() {   
        Constants.HOME_DIR = SystemUtil.getOS() == OSType.WINDOWS ? 
            "C:/Users/" + System.getProperty("user.name") + "/" :
            System.getProperty("user.home").contains("root") ? "/root/" : 
            "/home/" + System.getProperty("user.name") + "/";
        
        Constants.BASE_DIR = Constants.HOME_DIR + 
            (SystemUtil.getOS() == OSType.WINDOWS ? ".contaminus" :
            SystemUtil.getOS() == OSType.LINUX ? ".config/contaminus" :
            ".contaminus");
        
        Constants.CONFIG_FILE = Constants.BASE_DIR + "/config.properties";
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        Constants.LOGGER = LoggerFactory.getLogger(Constants.APP_NAME);
    }
    
    private static void initializeDirectories() {
        File baseDir = new File(Constants.BASE_DIR);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }
    
    private static void copyDefaultConfig() {
        File configFile = new File(Constants.CONFIG_FILE);
        if (!configFile.exists()) {
            try (InputStream in = MainVerticle.class.getClassLoader().getResourceAsStream("default.properties")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Constants.LOGGER.error("Default config file not found in resources");
                }
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to copy default config file", e);
            }
        }
    }
}
