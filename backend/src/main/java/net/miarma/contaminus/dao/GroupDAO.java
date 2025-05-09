package net.miarma.contaminus.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.contaminus.db.DataAccessObject;
import net.miarma.contaminus.db.DatabaseManager;
import net.miarma.contaminus.db.QueryBuilder;
import net.miarma.contaminus.entities.Group;

public class GroupDAO implements DataAccessObject<Group, Integer> {
	
private final DatabaseManager db;
	
	public GroupDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}
	
	@Override
	public Future<List<Group>> getAll() {
		Promise<List<Group>> promise = Promise.promise();
		String query = QueryBuilder.select(Group.class).build();
		db.execute(query, Group.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);
		return promise.future();
	}
	
	@Override
	public Future<Group> getById(Integer id) {
		Promise<Group> promise = Promise.promise();
		Group group = new Group();
		group.setGroupId(id);
		
		String query = QueryBuilder
				.select(Group.class)
				.where(group)
				.build();
		
		db.execute(query, Group.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Group> insert(Group t) {
		Promise<Group> promise = Promise.promise();
		String query = QueryBuilder.insert(t).build();
		
		db.execute(query, Group.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Group> update(Group t) {
		Promise<Group> promise = Promise.promise();
		String query = QueryBuilder.update(t).build();
		
		db.execute(query, Group.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

	@Override
	public Future<Group> delete(Integer id) {
		Promise<Group> promise = Promise.promise();
		Group group = new Group();
		group.setGroupId(id);
		
		String query = QueryBuilder.delete(group).build();
		
		db.execute(query, Group.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);
		
		return promise.future();
	}

}
