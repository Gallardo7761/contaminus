package net.miarma.contaminus.clase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class BroadcastVerticle extends AbstractVerticle {
	@Override
	public void start(Promise<Void> promise) {
		vertx.setPeriodic(2000, _a -> {
			vertx.eventBus().publish("broadcast.addr", "Ola");
		});
		try {
			promise.complete();
		} catch (Exception e) {
			promise.fail(e);
		}
	}
}
