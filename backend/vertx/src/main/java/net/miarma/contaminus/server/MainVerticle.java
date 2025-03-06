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
		
		getVertx().deployVerticle(new DatabaseVerticle(), options, result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 HttpServerVerticle desplegado.(http://localhost:8080)");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar HttpServerVerticle", result.cause());
			}
		});
		getVertx().deployVerticle(new ApiVerticle(), options, result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 ApiVerticle desplegado. (http://localhost:8081/api/v1)");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar ApiVerticle", result.cause());
			}
		});
		getVertx().deployVerticle(new HttpServerVerticle(), result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 DatabaseVerticle desplegado.");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar HttpServerVerticle", result.cause());
			}
		});
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		getVertx().deploymentIDs()
			.forEach(v -> getVertx().undeploy(v));
	}
	
}