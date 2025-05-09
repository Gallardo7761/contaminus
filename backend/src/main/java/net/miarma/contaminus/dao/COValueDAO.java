package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.COValue;

public class COValueDAO implements DataAccessObject<COValue, Integer> {

	private final DatabaseManager db;
	
	public COValueDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<COValue>> getAll() {
		Promise<List<COValue>> promise = Promise.promise();
		String query = QueryBuilder.select(COValue.class).build();
		db.execute(query, COValue.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<COValue> getById(Integer id) {
		Promise<COValue> promise = Promise.promise();
		COValue coValue = new COValue();
		coValue.setValueId(id);
		
		String query = QueryBuilder
				.select(COValue.class)
				.where(coValue)
				.build();
		
		db.execute(query, COValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<COValue> insert(COValue t) {
		Promise<COValue> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, COValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<COValue> update(COValue t) {
		Promise<COValue> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, COValue.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<COValue> delete(Integer id) {
		throw new UnsupportedOperationException("Cannot delete samples");
	}

}
