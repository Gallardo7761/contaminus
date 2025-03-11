package net.miarma.contaminus.clase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import net.miarma.contaminus.common.Constants;

public class ConsumerVerticle2 extends AbstractVerticle {
	@Override
	public void start(Promise<Void> promise) {
		vertx.eventBus().consumer("broadcast.addr", this::handleMsg);
		try {
			promise.complete();
		} catch (Exception e) {
			promise.fail(e);
		}
	}
	
	private void handleMsg(Message<String> msg) {
		Constants.LOGGER.info("Ola Broadcast soy Consumer2");
	}

}
