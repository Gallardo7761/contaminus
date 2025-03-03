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
            .put("url", config.getProperty("db.protocol") + "//" +
                            config.getProperty("db.host") + ":" +
                            config.getProperty("db.port") + "/" +
                            config.getProperty("db.name"))
            .put("user", config.getProperty("db.user"))
            .put("password", config.getProperty("db.pwd"))
            .put("max_pool_size", config.getProperty("db.poolSize"));	

        pool = JDBCPool.pool(vertx, dbConfig);
    }

    public JDBCPool getPool() {
        return pool;
    }
}
