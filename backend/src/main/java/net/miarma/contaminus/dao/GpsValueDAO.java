package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.GpsValue;

public class GpsValueDAO implements DataAccessObject<GpsValue, Integer> {

	private final DatabaseManager db;
	
	public GpsValueDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<GpsValue>> getAll() {
		Promise<List<GpsValue>> promise = Promise.promise();
		String query = QueryBuilder.select(GpsValue.class).build();
		db.execute(query, GpsValue.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<GpsValue> getById(Integer id) {
		Promise<GpsValue> promise = Promise.promise();
		GpsValue gpsValue = new GpsValue();
		gpsValue.setValueId(id);
		
		String query = QueryBuilder
				.select(GpsValue.class)
				.where(gpsValue)
				.build();
		
		db.execute(query, GpsValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<GpsValue> insert(GpsValue t) {
		Promise<GpsValue> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, GpsValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<GpsValue> update(GpsValue t) {
		Promise<GpsValue> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, GpsValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<GpsValue> delete(Integer id) {
		throw new UnsupportedOperationException("Cannot delete samples");
	}
}
