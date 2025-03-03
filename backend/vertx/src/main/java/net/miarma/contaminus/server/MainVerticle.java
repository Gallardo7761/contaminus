package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {
	
	@Override
	public void start(Promise<Void> startPromise) {
		getVertx().deployVerticle(new DatabaseVerticle());
		getVertx().deployVerticle(new HttpServerVerticle());
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		getVertx().deploymentIDs()
			.forEach(v -> getVertx().undeploy(v));
	}
	
}