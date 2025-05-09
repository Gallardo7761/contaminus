package net.miarma.contaminus.dao.views;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.ViewSensorHistory;

public class ViewSensorHistoryDAO implements DataAccessObject<ViewSensorHistory, String> {

	private final DatabaseManager db;

	public ViewSensorHistoryDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}

	@Override
	public Future<List<ViewSensorHistory>> getAll() {
		Promise<List<ViewSensorHistory>> promise = Promise.promise();
		String query = QueryBuilder.select(ViewSensorHistory.class).build();
		db.execute(query, ViewSensorHistory.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<ViewSensorHistory> getById(String id) {
		Promise<ViewSensorHistory> promise = Promise.promise();
		ViewSensorHistory viewSensorHistory = new ViewSensorHistory();
		viewSensorHistory.setDeviceId(id);
		
		String query = QueryBuilder
				.select(ViewSensorHistory.class)
				.where(viewSensorHistory)
				.build();
		
		db.execute(query, ViewSensorHistory.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<ViewSensorHistory> insert(ViewSensorHistory t) {
		throw new UnsupportedOperationException("Insert not supported for views");
	}

	@Override
	public Future<ViewSensorHistory> update(ViewSensorHistory t) {
		throw new UnsupportedOperationException("Update not supported for views");
	}

	@Override
	public Future<ViewSensorHistory> delete(String id) {
		throw new UnsupportedOperationException("Delete not supported for views");
	}
	

}
