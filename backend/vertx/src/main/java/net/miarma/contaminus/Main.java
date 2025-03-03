package net.miarma.contaminus;
import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import net.miarma.contaminus.p2p.VerticleConfig;

public class Main extends AbstractVerticle {

	private Gson gson;
	
	@Override
	public void start(Promise<Void> promise) {
		gson = new Gson();
		
		VerticleConfig config = new VerticleConfig();
		config.setNum(2);
		config.setName("[MESSAGE] ");
		
		DeploymentOptions options = new DeploymentOptions();
		options.setConfig(new JsonObject(gson.toJson(config)));
		
		String consumer1Name = ConsumerVerticle1.class.getName();
		getVertx().deployVerticle(consumer1Name, options, result -> {
			if (result.succeeded()) {
				System.out.println(consumer1Name + " (" + result.result() + ") ha sido desplegado correctamente");
			} else {
				result.cause().printStackTrace();
			}
		});
		
		String consumer2Name = ConsumerVerticle1.class.getName();
		getVertx().deployVerticle(consumer2Name, options, result -> {
			if (result.succeeded()) {
				System.out.println(consumer2Name + " (" + result.result() + ") ha sido desplegado correctamente");
			} else {
				result.cause().printStackTrace();
			}
		});
		
		String senderName = SenderVerticle.class.getName();
		getVertx().deployVerticle(senderName, options, result -> {
			if (result.succeeded()) {
				System.out.println(senderName + " (" + result.result() + ") ha sido desplegado correctamente");
			} else {
				result.cause().printStackTrace();
			}
		});
	}
	
	@Override
	public void stop(Promise<Void> stopFuture) throws Exception {
		getVertx().undeploy(ConsumerVerticle1.class.getName());
		getVertx().undeploy(SenderVerticle.class.getName());
		super.stop(stopFuture);
	}
}