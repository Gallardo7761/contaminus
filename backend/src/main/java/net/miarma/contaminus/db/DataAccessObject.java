package net.miarma.contaminus.db;

import java.util.List;

import io.vertx.core.Future;

public interface DataAccessObject<T, ID> {	
	Future<List<T>> getAll();
	Future<T> getById(ID id);
	Future<T> insert(T t);
	Future<T> update(T t);
	Future<T> delete(ID id);
}
