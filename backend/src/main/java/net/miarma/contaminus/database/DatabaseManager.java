package net.miarma.contaminus.database;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import net.miarma.contaminus.common.ConfigManager;
import net.miarma.contaminus.common.Constants;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final JDBCPool pool;

    @SuppressWarnings("deprecation")
	private DatabaseManager(Vertx vertx) {
        ConfigManager config = ConfigManager.getInstance();

        JsonObject dbConfig = new JsonObject()
                .put("jdbcUrl", config.getJdbcUrl())
                .put("username", config.getStringProperty("db.user"))
                .put("password", config.getStringProperty("db.pwd"))
                .put("max_pool_size", config.getIntProperty("db.poolSize"));

        pool = JDBCPool.pool(vertx, dbConfig);
    }

    public static synchronized DatabaseManager getInstance(Vertx vertx) {
        if (instance == null) {
            instance = new DatabaseManager(vertx);
        }
        return instance;
    }

    public Future<RowSet<Row>> testConnection() {
        return pool.query("SELECT 1").execute();
    }

    public <T> Future<List<T>> execute(String query, Class<T> clazz,
                                        Handler<List<T>> onSuccess, Handler<Throwable> onFailure) {
        return pool.query(query).execute()
                .map(rows -> {
                    List<T> results = new ArrayList<>();
                    for (Row row : rows) {
                        try {
                            Constructor<T> constructor = clazz.getConstructor(Row.class);
                            results.add(constructor.newInstance(row));
                        } catch (NoSuchMethodException | InstantiationException |
                                 IllegalAccessException | InvocationTargetException e) {
                            Constants.LOGGER.error("Error instantiating class: " + e.getMessage());
                        }
                    }
                    return results;
                })
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        onSuccess.handle(ar.result());
                    } else {
                        onFailure.handle(ar.cause());
                    }
                });
    }
}
