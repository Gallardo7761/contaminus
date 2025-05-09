package net.miarma.contaminus.dao.views;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.ViewSensorValue;

public class ViewSensorValueDAO implements DataAccessObject<ViewSensorValue, Integer> {

	private final DatabaseManager db;

	public ViewSensorValueDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}

	@Override
	public Future<List<ViewSensorValue>> getAll() {
		Promise<List<ViewSensorValue>> promise = Promise.promise();
		String query = QueryBuilder.select(ViewSensorValue.class).build();
		db.execute(query, ViewSensorValue.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<ViewSensorValue> getById(Integer id) {
		Promise<ViewSensorValue> promise = Promise.promise();
		ViewSensorValue view = new ViewSensorValue();
		view.setSensorId(id);
		
		String query = QueryBuilder
				.select(ViewSensorValue.class)
				.where(view)
				.build();
		
		db.execute(query, ViewSensorValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<ViewSensorValue> insert(ViewSensorValue t) {
		throw new UnsupportedOperationException("Insert not supported for views");
	}

	@Override
	public Future<ViewSensorValue> update(ViewSensorValue t) {
		throw new UnsupportedOperationException("Update not supported for views");
	}

	@Override
	public Future<ViewSensorValue> delete(Integer id) {
		throw new UnsupportedOperationException("Delete not supported for views");
	}

}
