package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;

public class HttpServerVerticle extends AbstractVerticle {   
	private ConfigManager configManager = ConfigManager.getInstance();

    @Override
    public void start() {
    	Constants.LOGGER.info("🟢 Iniciando HttpServerVerticle...");
        Router router = Router.router(vertx);
        router.route("/*").handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        
        vertx.createHttpServer().requestHandler(router).listen(
        		configManager.getIntProperty("webserver.port"), configManager.getStringProperty("inet.host"), result -> {
			if (result.succeeded()) {
				Constants.LOGGER.info(String.format("📡 HttpServerVerticle desplegado. (http://%s:%d)", 
						configManager.getStringProperty("inet.host"), configManager.getIntProperty("api.port"))
				);
			} else {
				Constants.LOGGER.error("❌ Error al desplegar HttpServerVerticle", result.cause());
			}
		});
    }

    
}
