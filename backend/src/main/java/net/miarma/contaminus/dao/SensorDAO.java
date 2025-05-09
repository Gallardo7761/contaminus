package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.Sensor;

public class SensorDAO implements DataAccessObject<Sensor, Integer> {

	private final DatabaseManager db;
	
	public SensorDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<Sensor>> getAll() {
		Promise<List<Sensor>> promise = Promise.promise();
		String query = QueryBuilder.select(Sensor.class).build();
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	public Future<List<Sensor>> getAllByDeviceId(String deviceId) {
		Promise<List<Sensor>> promise = Promise.promise();
		Sensor sensor = new Sensor();
		sensor.setDeviceId(deviceId);
		
		String query = QueryBuilder
				.select(Sensor.class)
				.where(sensor)
				.build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		
		return promise.future();
	}
	
	@Override
	public Future<Sensor> getById(Integer id) {
		Promise<Sensor> promise = Promise.promise();
		Sensor sensor = new Sensor();
		sensor.setSensorId(id);
		
		String query = QueryBuilder
				.select(Sensor.class)
				.where(sensor)
				.build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
	
	public Future<Sensor> getByIdAndDeviceId(Integer sensorId, String deviceId) {
		Promise<Sensor> promise = Promise.promise();
		Sensor sensor = new Sensor();
		sensor.setDeviceId(deviceId);
		sensor.setSensorId(sensorId);
		
		String query = QueryBuilder
				.select(Sensor.class)
				.where(sensor)
				.build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Sensor> insert(Sensor t) {
		Promise<Sensor> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Sensor> update(Sensor t) {
		Promise<Sensor> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Sensor> delete(Integer id) {
		Promise<Sensor> promise = Promise.promise();
		Sensor sensor = new Sensor();
		
		sensor.setSensorId(id);
		
		String query = QueryBuilder.delete(sensor).build();
		
		db.execute(query, Sensor.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
}
