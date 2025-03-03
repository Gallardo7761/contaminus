package net.miarma.contaminus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ConsumerVerticle2 extends AbstractVerticle {
	@Override
	public void start(Promise<Void> startPromise) {
		getVertx().eventBus().consumer("__addr_ConsumerBox", message -> {
			String customMessage = (String) message.body();
			System.out.println("[2] Mensaje recibido (" + message.address() + "): " + customMessage);
			String replyMessage = "[2] Mensaje recibido: \"" + message.body().toString() + "\"";
			message.reply(replyMessage);
		});
		startPromise.complete();
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		super.stop(stopPromise);
	}
}
