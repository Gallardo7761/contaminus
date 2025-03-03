package net.miarma.contaminus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

public class SenderVerticle extends AbstractVerticle {
	String verticleID = "";
	
	@Override
	public void start(Promise<Void> promise) {
		EventBus eventBus = vertx.eventBus();
		
		vertx.setPeriodic(4000, _id -> {
			String message = "Hola papu";
			eventBus.request("__addr_ConsumerBox", message, reply -> {
				Message<Object> res = reply.result();
				verticleID = res.address();
				if(reply.succeeded()) {
					String replyMsg = (String) res.body();
					System.out.println("Respuesta recibida (" + res.address() + "): " + replyMsg + "\n\n\n");
				} else {
					System.out.println("No ha habido respuesta");					
				}
			});
		});
	}
}
