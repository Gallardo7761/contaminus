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
        if (clazz.isAnnotationPresent(Table.class)) {
            Table annotation = clazz.getAnnotation(Table.class);
            return annotation.value(); // lee el nombre de la tabla desde la annotation
        }
        throw new IllegalArgumentException("Clase no tiene la anotación @TableName");
    }

    public String getQuery() {
        return query.toString();
    }
    
    public static <T> QueryBuilder select(Class<T> clazz, String... columns) {
        QueryBuilder qb = new QueryBuilder();
        String tableName = getTableName(clazz);

        qb.query.append("SELECT ");
        
        if (columns.length == 0) {
            qb.query.append("* ");
        } else {
            StringJoiner joiner = new StringJoiner(", ");
            for (String column : columns) {
                joiner.add(column);
            }
            qb.query.append(joiner).append(" ");
        }

        qb.query.append("FROM ").append(tableName).append(" ");
        return qb;
    }
    
    public static <T> QueryBuilder where(QueryBuilder qb, T object) {
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
        Class<?> clazz = object.getClass();
        QueryBuilder qb = select(clazz, columns);
        return where(qb, object);
    }

  
    public static <T> QueryBuilder insert(T object) {
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
				if(field.get(object) instanceof String) {
					values.add("'" + field.get(object) + "'");
				} else {
					values.add(field.get(object).toString());
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
		QueryBuilder qb = new QueryBuilder();
		String table = getTableName(object.getClass());
		qb.query.append("UPDATE ").append(table).append(" ");
		qb.query.append("SET ");
		StringJoiner joiner = new StringJoiner(", ");
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if(field.get(object) instanceof String) {
					joiner.add(field.getName() + " = '" + field.get(object) + "'");
				} else {
					joiner.add(field.getName() + " = " + field.get(object).toString());
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
			}
		}
		qb.query.append(joiner).append(" ");
		return qb;
    }
    
    public QueryBuilder orderBy(Optional<String> column, Optional<String> order) {
        if (column.isPresent()) {
        	sort = "ORDER BY " + column.get() + " ";
            if (order.isPresent()) {
            	sort += order.get().equalsIgnoreCase("asc") ? "ASC" : "DESC" + " ";
            }
        }
        return this;
    }
    
    public QueryBuilder limit(Optional<Integer> limitParam) {
    	limit = limitParam.isPresent() ? "LIMIT " + limitParam.get() + " " : "";
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
