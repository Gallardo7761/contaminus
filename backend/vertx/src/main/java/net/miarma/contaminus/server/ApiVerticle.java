package net.miarma.contaminus.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.Host;
import net.miarma.contaminus.database.QueryBuilder;

public class ApiVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        Constants.LOGGER.info("🟢 Iniciando ApiVerticle...");
        Router router = Router.router(vertx);
        
        Set<HttpMethod> allowedMethods = new HashSet<>(Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT)); // Por ejemplo
		Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));

        router.route().handler(CorsHandler.create()
                .addOrigin(Host.getOrigin())
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods));
        router.route().handler(BodyHandler.create());
        router.get(Constants.API_PREFIX + "/devices").blockingHandler(this::getAllDevices);
        router.get(Constants.API_PREFIX + "/devices/:id").blockingHandler(this::getDeviceById);
        router.post(Constants.API_PREFIX + "/devices").blockingHandler(this::insertSensor);
        router.get(Constants.API_PREFIX + "/status").handler(ctx -> ctx.json(new JsonObject().put("status", "OK")));

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(
                        Host.getApiPort(),
                        Host.getHost(),
                        result -> {
                            if (result.succeeded()) {
                                Constants.LOGGER.info(String.format(
                                    "📡 ApiVerticle desplegado. (http://%s:%d)", Host.getHost(), Host.getApiPort()
                                ));
                                startPromise.complete();
                            } else {
                                Constants.LOGGER.error("❌ Error al desplegar ApiVerticle", result.cause());
                                startPromise.fail(result.cause());
                            }
                        });
    }

    private void getAllDevices(RoutingContext context) {
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
                .from("v_DevicesMeasures")
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

    private void getDeviceById(RoutingContext context) {
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
        
        String id = context.request().getParam("id");
        String query = QueryBuilder
                .select("*")
                .from("v_DevicesMeasures")
                .where("deviceId = ?", id)
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
    
    private void insertSensor(RoutingContext context) {
        JsonObject body = context.body().asJsonObject();

        if (body == null) {
            context.fail(400, new IllegalArgumentException("Body is missing or invalid"));
            return;
        }
        
        Integer deviceId = body.getInteger("deviceId");
        String sensorType = body.getString("sensorType");
        Float lat = body.getFloat("lat");
        Float lon = body.getFloat("lon");
        Float value = body.getFloat("value");

        if (sensorType == null || lat == null || lon == null || value == null) {
            context.fail(400, new IllegalArgumentException("Missing required fields"));
            return;
        }

        String query = QueryBuilder
                .insert("measures", "deviceId", "sensorType", "lat", "lon", "value")
                .values(deviceId, sensorType, lat, lon, value)
                .build();
        
        vertx.eventBus().request("db.query", query, req -> {
            if (req.succeeded()) {
                context.json(new JsonObject().put("result", "OK"));
            } else {
                context.fail(500, req.cause());
            }
        });
    }

    
}