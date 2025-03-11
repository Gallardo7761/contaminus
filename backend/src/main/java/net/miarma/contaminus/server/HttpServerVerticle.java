package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.Host;

public class HttpServerVerticle extends AbstractVerticle {   
	
    @Override
    public void start() {
    	Constants.LOGGER.info("🟢 Iniciando HttpServerVerticle...");
        Router router = Router.router(vertx);
        
        router.route("/*").handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        
        router.route("/dashboard/*").handler(ctx -> {
            ctx.reroute("/index.html");
        });

        vertx.createHttpServer().requestHandler(router).listen(
        		Host.getWebserverPort(), Host.getHost(), result -> {
			if (result.succeeded()) {
				Constants.LOGGER.info(String.format("📡 HttpServerVerticle desplegado. (http://%s:%d)", 
						Host.getHost(), Host.getWebserverPort())
				);
			} else {
				Constants.LOGGER.error("❌ Error al desplegar HttpServerVerticle", result.cause());
			}
		});
    }
}
