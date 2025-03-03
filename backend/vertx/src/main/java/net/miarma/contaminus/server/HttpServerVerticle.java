package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import net.miarma.contaminus.common.Constants;

public class HttpServerVerticle extends AbstractVerticle {    
    @Override
    public void start() {
    	Constants.LOGGER.info("🟢 Iniciando HttpServerVerticle...");
    	        
        Router router = Router.router(vertx);
        router.route("/*").handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        
        router.get(Constants.API_PREFIX + "/sensors").blockingHandler(this::getAllSensors);
        router.get(Constants.API_PREFIX + "/status").handler(ctx -> 
            ctx.json(new JsonObject().put("status", "OK"))
        );

        vertx.createHttpServer().requestHandler(router).listen(80, response -> {
            if (response.succeeded()) {
                Constants.LOGGER.info("🚀 Servidor HTTP desplegado en http://localhost:80");
            } else {
                Constants.LOGGER.error("❌ Error al desplegar el servidor HTTP", response.cause());
            }
        });
    }

    private void getAllSensors(RoutingContext context) {
        vertx.eventBus().request("db.query", "SELECT * FROM sensor_mq_data", new DeliveryOptions(), ar -> {
            if (ar.succeeded()) {
                Message<Object> result = ar.result();
                JsonArray jsonArray = (JsonArray) result.body();
                context.json(jsonArray);
            } else {
                context.fail(500, ar.cause());
            }
        });
    }
}
