package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import net.miarma.contaminus.common.Constants;

public class MainVerticle extends AbstractVerticle {
	
	@Override
	public void start(Promise<Void> startPromise) {
		final DeploymentOptions options = new DeploymentOptions();
		options.setThreadingModel(ThreadingModel.WORKER);
		
		getVertx().deployVerticle(new DatabaseVerticle(), options);
		getVertx().deployVerticle(new ApiVerticle(), options);
		getVertx().deployVerticle(new HttpServerVerticle());
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		getVertx().deploymentIDs()
			.forEach(v -> getVertx().undeploy(v));
	}
	
}