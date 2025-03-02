package net.miarma.contaminus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Main extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startFuture) {
		vertx.createHttpServer().requestHandler(r -> {
			String file = r.path().equals("/") ? "index.html" : r.path().substring(1);
			r.response().sendFile("webroot/" + file);
		}).listen(80, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
	}
}