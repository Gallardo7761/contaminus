package net.miarma.contaminus.dao.views;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.ViewPollutionMap;

@Table("v_pollution_map")
public class ViewPollutionMapDAO implements DataAccessObject<ViewPollutionMap, String> {
	
	private final DatabaseManager db;
	public ViewPollutionMapDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<ViewPollutionMap>> getAll() {
		Promise<List<ViewPollutionMap>> promise = Promise.promise();
		String query = QueryBuilder.select(ViewPollutionMap.class).build();
		db.execute(query, ViewPollutionMap.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<ViewPollutionMap> getById(String id) {
		Promise<ViewPollutionMap> promise = Promise.promise();
		ViewPollutionMap view = new ViewPollutionMap();
		view.setDeviceId(id);
		
		String query = QueryBuilder
				.select(ViewPollutionMap.class)
				.where(view)
				.build();
		
		db.execute(query, ViewPollutionMap.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
	
	@Override
	public Future<ViewPollutionMap> insert(ViewPollutionMap t) {
		throw new UnsupportedOperationException("Insert not supported for views");
	}
	
	@Override
	public Future<ViewPollutionMap> update(ViewPollutionMap t) {
		throw new UnsupportedOperationException("Update not supported for views");
	}
	
	@Override
	public Future<ViewPollutionMap> delete(String id) {
		throw new UnsupportedOperationException("Delete not supported for views");
	}
}
