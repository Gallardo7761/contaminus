package net.miarma.contaminus.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.LocalDateTimeSerializer;
import net.miarma.contaminus.database.DatabaseManager;

public class DatabaseVerticle extends AbstractVerticle {
    private JDBCPool pool;
    private EventBus eventBus;
	private Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
	        .create();;


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
        Constants.LOGGER.info("📥 Query: " + query);

        if(query == null || query.isEmpty()) {
            msg.fail(400, "Empty query");
            return;
        }

        if(query.startsWith("SELECT")) {
            handleSelectQuery(query, msg);
        } else if(query.startsWith("INSERT")) {
            handleInsertQuery(query, msg);
        } else if(query.startsWith("UPDATE")) {
            handleUpdateQuery(query, msg);
        } else {
            msg.fail(400, "Invalid operation");
        }
    }

    private void handleSelectQuery(String query, Message<String> msg) {
        pool.query(query).execute()
            .onSuccess(res -> {
                List<Map<String, Object>> rowsList = new ArrayList<>();
                
                for (Row row : res) {
                    Map<String, Object> rowMap = new HashMap<>();
                    for (int i = 0; i < row.size(); i++) {
                        String columnName = res.columnsNames().get(i);
                        Object columnValue = row.getValue(i);
                        rowMap.put(columnName, columnValue);
                    }
                    rowsList.add(rowMap);
                }

                String jsonResponse = gson.toJson(rowsList);
                msg.reply(jsonResponse);
            })
            .onFailure(err -> msg.fail(500, err.getMessage()));
    }

    @SuppressWarnings("unused")
    private void handleInsertQuery(String query, Message<String> msg) {
    	pool.query(query).execute()
        .onSuccess(_res -> {
            JsonObject response = new JsonObject();
            response.put("status", "success");
            String jsonResponse = gson.toJson(response);
            msg.reply(jsonResponse);
        })
        .onFailure(err -> msg.fail(500, err.getMessage()));
    }

    @SuppressWarnings("unused")
    private void handleUpdateQuery(String query, Message<String> msg) {
    	pool.query(query).execute()
        .onSuccess(_res -> {
            JsonObject response = new JsonObject();
            response.put("status", "updated");
            String jsonResponse = gson.toJson(response);
            msg.reply(jsonResponse);
        })
        .onFailure(err -> msg.fail(500, err.getMessage()));
    }
}
