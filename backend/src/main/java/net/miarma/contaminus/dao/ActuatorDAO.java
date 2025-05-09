package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.Actuator;

public class ActuatorDAO implements DataAccessObject<Actuator, Integer>{

	private final DatabaseManager db;
	
	public ActuatorDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<Actuator>> getAll() {
		Promise<List<Actuator>> promise = Promise.promise();
		String query = QueryBuilder.select(Actuator.class).build();
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	public Future<List<Actuator>> getAllByDeviceId(String deviceId) {
		Promise<List<Actuator>> promise = Promise.promise();
		Actuator actuator = new Actuator();
		actuator.setDeviceId(deviceId);
		
		String query = QueryBuilder
				.select(Actuator.class)
				.where(actuator)
				.build();
		
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		
		return promise.future();
	}
	
	@Override
	public Future<Actuator> getById(Integer id) {
		Promise<Actuator> promise = Promise.promise();
		Actuator actuator = new Actuator();
		actuator.setActuatorId(id);
		
		String query = QueryBuilder
				.select(Actuator.class)
				.where(actuator)
				.build();
		
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
	
	public Future<Actuator> getByIdAndDeviceId(Integer actuatorId, String deviceId) {
		Promise<Actuator> promise = Promise.promise();
		Actuator actuator = new Actuator();
		actuator.setDeviceId(deviceId);
		actuator.setActuatorId(actuatorId);
		
		String query = QueryBuilder
				.select(Actuator.class)
				.where(actuator)
				.build();
		
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Actuator> insert(Actuator t) {
		Promise<Actuator> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Actuator> update(Actuator t) {
		Promise<Actuator> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, Actuator.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Actuator> delete(Integer id) {
		Promise<Actuator> promise = Promise.promise();
		Actuator actuator = new Actuator();
		actuator.setActuatorId(id);
		
		String query = QueryBuilder.delete(actuator).build();
		
		db.executeOne(query, Actuator.class,
			_ -> promise.complete(actuator),
			promise::fail
		);
		
		return promise.future();
	}
}
