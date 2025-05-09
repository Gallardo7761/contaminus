package net.miarma.contaminus.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import net.miarma.contaminus.common.Constants;

public class DatabaseManager {

	private static DatabaseManager instance;
	private final Pool pool;

	private DatabaseManager(Pool pool) {
		this.pool = pool;
	}

	public static synchronized DatabaseManager getInstance(Pool pool) {
		if (instance == null) {
			instance = new DatabaseManager(pool);
		}
		return instance;
	}

	public Pool getPool() {
		return pool;
	}

	public Future<RowSet<Row>> testConnection() {
		return pool.query("SELECT 1").execute();
	}

	public <T> Future<List<T>> execute(String query, Class<T> clazz, Handler<List<T>> onSuccess,
			Handler<Throwable> onFailure) {
		return pool.query(query).execute().map(rows -> {
			List<T> results = new ArrayList<>();
			for (Row row : rows) {
				try {
					Constructor<T> constructor = clazz.getConstructor(Row.class);
					results.add(constructor.newInstance(row));
				} catch (NoSuchMethodException | InstantiationException | IllegalAccessException
						| InvocationTargetException e) {
					Constants.LOGGER.error("Error instantiating class: " + e.getMessage());
				}
			}
			return results;
		}).onComplete(ar -> {
			if (ar.succeeded()) {
				onSuccess.handle(ar.result());
			} else {
				onFailure.handle(ar.cause());
			}
		});
	}

	public <T> Future<T> executeOne(String query, Class<T> clazz, Handler<T> onSuccess, Handler<Throwable> onFailure) {
		return pool.query(query).execute().map(rows -> {
			for (Row row : rows) {
				try {
					Constructor<T> constructor = clazz.getConstructor(Row.class);
					return constructor.newInstance(row);
				} catch (Exception e) {
					Constants.LOGGER.error("Error instantiating class: " + e.getMessage());
				}
			}
			return null; // Si no hay filas
		}).onComplete(ar -> {
			if (ar.succeeded()) {
				onSuccess.handle(ar.result());
			} else {
				onFailure.handle(ar.cause());
			}
		});
	}

}
