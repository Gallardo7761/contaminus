package net.miarma.contaminus.dao.views;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.ViewLatestValues;

public class ViewLatestValuesDAO implements DataAccessObject<ViewLatestValues, String> {

	private final DatabaseManager db;

	public ViewLatestValuesDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}

	@Override
	public Future<List<ViewLatestValues>> getAll() {
		Promise<List<ViewLatestValues>> promise = Promise.promise();
		String query = QueryBuilder.select(ViewLatestValues.class).build();
		db.execute(query, ViewLatestValues.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<ViewLatestValues> getById(String id) {
		Promise<ViewLatestValues> promise = Promise.promise();
		ViewLatestValues view = new ViewLatestValues();
		view.setDeviceId(id);
		
		String query = QueryBuilder
				.select(ViewLatestValues.class)
				.where(view)
				.build();
		
		db.execute(query, ViewLatestValues.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<ViewLatestValues> insert(ViewLatestValues t) {
		throw new UnsupportedOperationException("Insert not supported for views");
	}

	@Override
	public Future<ViewLatestValues> update(ViewLatestValues t) {
		throw new UnsupportedOperationException("Update not supported for views");
	}

	@Override
	public Future<ViewLatestValues> delete(String id) {
		throw new UnsupportedOperationException("Delete not supported for views");
	}

}
