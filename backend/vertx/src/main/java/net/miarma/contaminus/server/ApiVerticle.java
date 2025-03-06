package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.QueryBuilder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ApiVerticle extends AbstractVerticle {
    private ConfigManager configManager = ConfigManager.getInstance();

    @Override
    public void start(Promise<Void> startPromise) {
        Constants.LOGGER.info("🟢 Iniciando ApiVerticle...");
        Router router = Router.router(vertx);

        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(io.vertx.core.http.HttpMethod.GET);
        allowedMethods.add(io.vertx.core.http.HttpMethod.POST);
        allowedMethods.add(io.vertx.core.http.HttpMethod.OPTIONS);

        router.route().handler(CorsHandler.create()
                .addOrigin("http://"+configManager.getStringProperty("inet.host")+":"+configManager.getIntProperty("webserver.port"))
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));

        router.get(Constants.API_PREFIX + "/sensors").blockingHandler(this::getAllSensors);
        router.get(Constants.API_PREFIX + "/sensors/:id").blockingHandler(this::getSensorById);
        router.get(Constants.API_PREFIX + "/status").handler(ctx -> ctx.json(new JsonObject().put("status", "OK")));

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(
                        configManager.getIntProperty("api.port"),
                        configManager.getStringProperty("inet.host"),
                        result -> {
                            if (result.succeeded()) {
                                Constants.LOGGER.info(String.format(
                                        "📡 ApiVerticle desplegado. (http://%s:%d)",
                                        configManager.getStringProperty("inet.host"),
                                        configManager.getIntProperty("api.port")
                                ));
                                startPromise.complete();
                            } else {
                                Constants.LOGGER.error("❌ Error al desplegar ApiVerticle", result.cause());
                                startPromise.fail(result.cause());
                            }
                        });
    }

    private void getAllSensors(RoutingContext context) {
        Optional<String> sort = Optional.ofNullable(context.request().getParam("_sort"));
        Optional<String> order = Optional.ofNullable(context.request().getParam("_order"));
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

        vertx.eventBus().request("db.query", query, req -> {
            if (req.succeeded()) {
                Message<Object> result = req.result();
                JsonArray jsonArray = (JsonArray) result.body();
                context.json(jsonArray);
            } else {
                context.fail(500, req.cause());
            }
        });
    }

    private void getSensorById(RoutingContext context) {
        String id = context.request().getParam("id");
        String query = QueryBuilder
                .select("*")
                .from("sensor_mq_data")
                .where("id = ?", id)
                .build();

        vertx.eventBus().request("db.query", query, req -> {
            if (req.succeeded()) {
                Message<Object> result = req.result();
                JsonArray jsonArray = (JsonArray) result.body();
                context.json(jsonArray);
            } else {
                context.fail(500, req.cause());
            }
        });
    }
}