package net.miarma.contaminus.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.Table;

public class QueryBuilder {
    private StringBuilder query;
    private String sort;
    private String order;
    private String limit;
    
    public QueryBuilder() {
        this.query = new StringBuilder();
    }
    
    private static <T> String getTableName(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        
        if (clazz.isAnnotationPresent(Table.class)) {
            Table annotation = clazz.getAnnotation(Table.class);
            return annotation.value();
        }
        throw new IllegalArgumentException("Class does not have @Table annotation");
    }

    public String getQuery() {
        return query.toString();
    }
    
    public static <T> QueryBuilder select(Class<T> clazz, String... columns) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        QueryBuilder qb = new QueryBuilder();
        String tableName = getTableName(clazz);

        qb.query.append("SELECT ");
        
        if (columns.length == 0) {
            qb.query.append("* ");
        } else {
            StringJoiner joiner = new StringJoiner(", ");
            for (String column : columns) {
                if (column != null) {
                    joiner.add(column);
                }
            }
            qb.query.append(joiner).append(" ");
        }

        qb.query.append("FROM ").append(tableName).append(" ");
        return qb;
    }
    
    public static <T> QueryBuilder where(QueryBuilder qb, T object) {
        if (qb == null || object == null) {
            throw new IllegalArgumentException("QueryBuilder and object cannot be null");
        }

        List<String> conditions = new ArrayList<>();
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value != null) {
                    if (value instanceof String) {
                        conditions.add(field.getName() + " = '" + value + "'");
                    } else {
                        conditions.add(field.getName() + " = " + value);
                    }
                }
            } catch (IllegalAccessException e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }

        if (!conditions.isEmpty()) {
            qb.query.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }
        
        return qb;
    }
    
    public static <T> QueryBuilder select(T object, String... columns) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        Class<?> clazz = object.getClass();
        QueryBuilder qb = select(clazz, columns);
        return where(qb, object);
    }

    public static <T> QueryBuilder insert(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

		QueryBuilder qb = new QueryBuilder();
		String table = getTableName(object.getClass());
		qb.query.append("INSERT INTO ").append(table).append(" ");
		qb.query.append("(");
		StringJoiner columns = new StringJoiner(", ");
		StringJoiner values = new StringJoiner(", ");
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				columns.add(field.getName());
				Object fieldValue = field.get(object);
				if (fieldValue != null) {
					if (fieldValue instanceof String) {
						values.add("'" + fieldValue + "'");
					} else {
						values.add(fieldValue.toString());
					}
				} else {
					values.add("NULL");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
			}
		}
		qb.query.append(columns).append(") ");
		qb.query.append("VALUES (").append(values).append(") ");
		return qb;
    }
    
    public static <T> QueryBuilder update(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

		QueryBuilder qb = new QueryBuilder();
		String table = getTableName(object.getClass());
		qb.query.append("UPDATE ").append(table).append(" ");
		qb.query.append("SET ");
		StringJoiner joiner = new StringJoiner(", ");
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object fieldValue = field.get(object);
				if (fieldValue != null) {
					if (fieldValue instanceof String) {
						joiner.add(field.getName() + " = '" + fieldValue + "'");
					} else {
						joiner.add(field.getName() + " = " + fieldValue.toString());
					}
				} else {
					joiner.add(field.getName() + " = NULL");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
			}
		}
		qb.query.append(joiner).append(" ");
		return qb;
    }
    
    public QueryBuilder orderBy(Optional<String> column, Optional<String> order) {
        column.ifPresent(c -> {
            sort = "ORDER BY " + c + " ";
            order.ifPresent(o -> {
                sort += o.equalsIgnoreCase("asc") ? "ASC" : "DESC" + " ";
            });
        });
        return this;
    }
    
    public QueryBuilder limit(Optional<Integer> limitParam) {
    	limitParam.ifPresent(param -> limit = "LIMIT " + param + " ");
        return this;
    }

    public String build() {
        if (order != null && !order.isEmpty()) {
        	query.append(order);
        }
        if (sort != null && !sort.isEmpty()) {
        	query.append(sort);
        }
        if (limit != null && !limit.isEmpty()) {
        	query.append(limit);
        }
        return query.toString().trim() + ";";
    }
}
