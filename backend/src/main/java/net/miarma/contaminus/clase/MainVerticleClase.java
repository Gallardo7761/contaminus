package net.miarma.contaminus.clase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import net.miarma.contaminus.common.Constants;

public class MainVerticleClase extends AbstractVerticle {
	@Override
	public void start(Promise<Void> promise) {
		vertx.deployVerticle(new BroadcastVerticle(), result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 BroadcastVerticle desplegado");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar BroadcastVerticle", result.cause());
			}
		});
		vertx.deployVerticle(new ConsumerVerticle1(), result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 ConsumerVerticle1 desplegado");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar ConsumerVerticle1", result.cause());
			}
		});
		vertx.deployVerticle(new ConsumerVerticle2(), result -> {
			if(result.succeeded()) {
				Constants.LOGGER.info("📡 ConsumerVerticle2 desplegado");
			} else {
				Constants.LOGGER.error("❌ Error al desplegar ConsumerVerticle2", result.cause());
			}
		});
	}
}
