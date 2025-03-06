package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import net.miarma.contaminus.common.Constants;

public class HttpServerVerticle extends AbstractVerticle {    
    @Override
    public void start() {
    	Constants.LOGGER.info("🟢 Iniciando HttpServerVerticle...");
        Router router = Router.router(vertx);
        router.route("/*").handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    
}
