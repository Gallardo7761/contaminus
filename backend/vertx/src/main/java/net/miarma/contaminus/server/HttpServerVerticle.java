package net.miarma.contaminus.server;

import java.util.Optional;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.QueryBuilder;

public class HttpServerVerticle extends AbstractVerticle {    
    @Override
    public void start() {
    	Constants.LOGGER.info("🟢 Iniciando HttpServerVerticle...");
    	        
        Router router = Router.router(vertx);
        router.route("/*").handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        
        router.get(Constants.API_PREFIX + "/sensors").blockingHandler(this::getAllSensors);
        router.get(Constants.API_PREFIX + "/sensors/:id").blockingHandler(this::getSensorById);
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
    	Optional<String> sort = Optional.ofNullable(context.request().getParam("_sort"));
    	Optional<String> order = Optional.ofNullable(context.request().getParam("_order"));
    	// forma tela de rara que me ha dado chatgpt para parsear esto XD
    	Optional<Integer> limit = Optional.ofNullable(context.request().getParam("_limit"))
    		    .map(s -> {
    		        try {
    		            return Integer.parseInt(s);
    		        } catch (NumberFormatException e) {
    		            return null;
    		        }
    		    });
    	
    	
    	String query = QueryBuilder
    			.select("*")
    			.from("sensor_mq_data")
    			.orderBy(sort, order)
    			.limit(limit)
    			.build();
    	
        vertx.eventBus().request("db.query", query, new DeliveryOptions(), ar -> {
            if (ar.succeeded()) {
                Message<Object> result = ar.result();
                JsonArray jsonArray = (JsonArray) result.body();
                context.json(jsonArray);
            } else {
                context.fail(500, ar.cause());
            }
        });
    }
    
    
    private void getSensorById(RoutingContext context) {
		String id = context.request().getParam("id");
		
		String query = QueryBuilder
				.select("*")
				.from("sensor_mq_data")
				.where("id = " + id)
				.build();
		
		vertx.eventBus().request("db.query", query, new DeliveryOptions(), ar -> {
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
