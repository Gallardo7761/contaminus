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

    @SuppressWarnings("unused")
	private void handleDatabaseQuery(Message<String> msg) {
        String query = msg.body();
        Constants.LOGGER.info("📥 Query: " + query);

        if(query == null || query.isEmpty()) {
			msg.fail(400, "Empty query");
			return;
		}
        
        if(query.startsWith("SELECT")) {
        	pool.query(query).execute()
	            .onSuccess(res -> {
	                RowSet<Row> rows = res;
	                JsonArray jsonArray = new JsonArray();
	                for (Row row : rows) {
	                    jsonArray.add(new JsonObject()
	                        .put("deviceId", row.getInteger("deviceId"))
	                        .put("deviceName", row.getString("deviceName"))
	                        .put("measureId", row.getInteger("measureId"))
	                        .put("sensorType", row.getString("sensorType"))
	                        .put("lat", row.getFloat("lat"))
	                        .put("lon", row.getFloat("lon"))
	                        .put("value", row.getFloat("value"))
	                        .put("timestamp", row.getLocalDateTime("timestamp").toString())
	                    );
	                }
	                msg.reply(jsonArray);
	            })
	            .onFailure(err -> msg.fail(500, err.getMessage()));
        } else if(query.startsWith("INSERT")) {
        	pool.query(msg.body()).execute()
		        .onSuccess(_res -> msg.reply(new JsonObject().put("status", "success")))
		        .onFailure(err -> msg.fail(500, err.getMessage()));
		} else {
			msg.fail(400, "Invalid operation");
		}

    }
}
