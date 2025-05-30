package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.Device;

public class DeviceDAO implements DataAccessObject<Device, String> {

	private final DatabaseManager db;
	
	public DeviceDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<Device>> getAll() {
		Promise<List<Device>> promise = Promise.promise();
		String query = QueryBuilder.select(Device.class).build();
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	public Future<List<Device>> getAllByGroupId(Integer groupId) {
		Promise<List<Device>> promise = Promise.promise();
		Device device = new Device();
		device.setGroupId(groupId);
		
		String query = QueryBuilder
				.select(Device.class)
				.where(device)
				.build();
		
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		
		return promise.future();
	}
	
	@Override
	public Future<Device> getById(String id) {
		Promise<Device> promise = Promise.promise();
		Device device = new Device();
		device.setDeviceId(id);
		
		String query = QueryBuilder
				.select(Device.class)
				.where(device)
				.build();
		
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
	
	public Future<Device> getByIdAndGroupId(String id, Integer groupId) {
		Promise<Device> promise = Promise.promise();
		Device device = new Device();
		device.setDeviceId(id);
		device.setGroupId(groupId);
		
		String query = QueryBuilder
				.select(Device.class)
				.where(device)
				.build();
				
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Device> insert(Device t) {
		Promise<Device> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Device> update(Device t) {
		Promise<Device> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Device> delete(String id) {
		Promise<Device> promise = Promise.promise();
		Device device = new Device();
		device.setDeviceId(id);
		String query = QueryBuilder.delete(device).build();
		
		db.execute(query, Device.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}
}
