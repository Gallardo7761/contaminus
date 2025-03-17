package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;

public class WebServerVerticle extends AbstractVerticle {   
	private ConfigManager configManager;
		
	public WebServerVerticle() {
		configManager = ConfigManager.getInstance();
	}
	
    @Override
    public void start(Promise<Void> startPromise) {    	
    	Constants.LOGGER.info("📡 Iniciando WebServerVerticle...");

        Router router = Router.router(vertx);
                
        router.route("/*")
        	.handler(
    			StaticHandler.create(configManager.getWebRoot())
    			.setCachingEnabled(false)
    			.setDefaultContentEncoding("UTF-8")
			);
        
        router.route("/dashboard/*").handler(ctx -> {
            ctx.reroute("/index.html");
        });

        vertx.createHttpServer()
			.requestHandler(router)
			.listen(configManager.getWebserverPort(), configManager.getHost());
        
        startPromise.complete();
    }
}
