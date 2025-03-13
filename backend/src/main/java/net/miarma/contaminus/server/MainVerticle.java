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
	    
	    getVertx().deployVerticle(new DataLayerAPIVerticle(), options);
	    //getVertx().deployVerticle(new LogicLayerAPIVerticle(), options);
	    //getVertx().deployVerticle(new HttpServerVerticle());
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		getVertx().deploymentIDs()
			.forEach(v -> getVertx().undeploy(v));
	}	
}