package net.miarma.contaminus.db;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import net.miarma.contaminus.common.ConfigManager;

public class DatabaseProvider {
    public static Pool createPool(Vertx vertx, ConfigManager config) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(config.getIntProperty("db.port"))
            .setHost(config.getStringProperty("db.host"))
            .setDatabase(config.getStringProperty("db.name"))
            .setUser(config.getStringProperty("db.user"))
            .setPassword(config.getStringProperty("db.password"));

        PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
        return Pool.pool(vertx, connectOptions, poolOptions);
    }
}
