package net.miarma.contaminus.database;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import net.miarma.contaminus.common.ConfigManager;

public class DatabaseManager {
    private final JDBCPool pool;

    @SuppressWarnings("deprecation")
	public DatabaseManager(Vertx vertx) {
        ConfigManager config = ConfigManager.getInstance();

        JsonObject dbConfig = new JsonObject()
            .put("url", config.getStringProperty("db.protocol") + "//" +
                            config.getStringProperty("db.host") + ":" +
                            config.getStringProperty("db.port") + "/" +
                            config.getStringProperty("db.name"))
            .put("user", config.getStringProperty("db.user"))
            .put("password", config.getStringProperty("db.pwd"))
            .put("max_pool_size", config.getStringProperty("db.poolSize"));	

        pool = JDBCPool.pool(vertx, dbConfig);
    }

    public JDBCPool getPool() {
        return pool;
    }
}
