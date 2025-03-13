package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;

public class MainVerticle extends AbstractVerticle {
	
	@Override
	public void start(Promise<Void> startPromise) {
	    final DeploymentOptions options = new DeploymentOptions();
	    options.setThreadingModel(ThreadingModel.WORKER);
	    	    
	    String enabledVerticles = System.getProperty("vertx.options", "");
	    	    
	    if (enabledVerticles.contains("data")) {
	        getVertx().deployVerticle(new DataLayerAPIVerticle(), options);
	    }
	    if (enabledVerticles.contains("business")) {
	        getVertx().deployVerticle(new LogicLayerAPIVerticle(), options);
	    }
	    if (enabledVerticles.contains("web")) {
	        getVertx().deployVerticle(new HttpServerVerticle());
	    }
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		getVertx().deploymentIDs()
			.forEach(v -> getVertx().undeploy(v));
	}
	
	public static void main(String[] args) {
	    System.setProperty("vertx.options", String.join(",", args));
	    io.vertx.core.Launcher.executeCommand("run", MainVerticle.class.getName());
	}
	
}