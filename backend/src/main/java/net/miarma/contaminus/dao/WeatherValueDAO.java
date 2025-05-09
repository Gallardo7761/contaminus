package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.WeatherValue;

public class WeatherValueDAO implements DataAccessObject<WeatherValue, Integer> {

	private final DatabaseManager db;
	
	public WeatherValueDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<WeatherValue>> getAll() {
		Promise<List<WeatherValue>> promise = Promise.promise();
		String query = QueryBuilder.select(WeatherValue.class).build();
		db.execute(query, WeatherValue.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<WeatherValue> getById(Integer id) {
		Promise<WeatherValue> promise = Promise.promise();
		WeatherValue weatherValue = new WeatherValue();
		weatherValue.setValueId(id);
		
		String query = QueryBuilder
				.select(WeatherValue.class)
				.where(weatherValue)
				.build();
		
		db.execute(query, WeatherValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<WeatherValue> insert(WeatherValue t) {
		Promise<WeatherValue> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, WeatherValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<WeatherValue> update(WeatherValue t) {
		Promise<WeatherValue> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, WeatherValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<WeatherValue> delete(Integer id) {
		throw new UnsupportedOperationException("Cannot delete samples");
	}
}
