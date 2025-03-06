package net.miarma.contaminus.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.database.DatabaseManager;

public class DatabaseVerticle extends AbstractVerticle {
    private JDBCPool pool;
    private EventBus eventBus;

    @SuppressWarnings("unused")
	@Override
    public void start(Promise<Void> startPromise) {
    	Constants.LOGGER.info("🟢 Iniciando DatabaseVerticle...");
    	
        DatabaseManager dbManager = new DatabaseManager(vertx);
        pool = dbManager.getPool();
        eventBus = vertx.eventBus();

        pool.query("SELECT 1")
            .execute()
            .onSuccess(_res -> {
                Constants.LOGGER.info("✅ Database connection ok");
                Constants.LOGGER.info("📡 DatabaseVerticle desplegado");
                startPromise.complete();
            })
            .onFailure(err -> {
                Constants.LOGGER.error("❌ Database connection failed");
                Constants.LOGGER.error("❌ Error al desplegar DatabaseVerticle", err);
                startPromise.fail(err);
            });

        eventBus.consumer("db.query", this::handleDatabaseQuery);
    }

    private void handleDatabaseQuery(Message<String> msg) {
        String query = msg.body();

        pool.query(query).execute()
            .onSuccess(res -> {
                RowSet<Row> rows = res;
                JsonArray jsonArray = new JsonArray();
                for (Row row : rows) {
                    jsonArray.add(new JsonObject()
                        .put("id", row.getInteger("id"))
                        .put("sensorType", row.getString("sensor_type"))
                        .put("value", row.getFloat("value"))
                        .put("lat", row.getFloat("lat"))
                        .put("lon", row.getFloat("lon"))
                        .put("timestamp", row.getLocalDateTime("timestamp").toString())
                    );
                }
                msg.reply(jsonArray);
            })
            .onFailure(err -> msg.fail(500, err.getMessage()));
    }
}
