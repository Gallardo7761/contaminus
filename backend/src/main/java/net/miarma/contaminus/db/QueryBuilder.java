package net.miarma.contaminus.db;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import net.miarma.contaminus.common.Constants;
import net.miarma.contaminus.common.Table;


public class QueryBuilder {
    private final StringBuilder query;
    private String sort;
    private String order;
    private String limit;
    private Class<?> entityClass;

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

    private static Object extractValue(Object fieldValue) {
        if (fieldValue instanceof Enum<?>) {
            try {
                var method = fieldValue.getClass().getMethod("getValue");
                return method.invoke(fieldValue);
            } catch (Exception e) {
                return ((Enum<?>) fieldValue).name();
            }
        }
        return fieldValue;
    }

    public static <T> QueryBuilder select(Class<T> clazz, String... columns) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        QueryBuilder qb = new QueryBuilder();
        qb.entityClass = clazz;
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

    public QueryBuilder where(Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return this;
        }

        Set<String> validFields = entityClass != null
            ? Arrays.stream(entityClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet())
            : Collections.emptySet();

        List<String> conditions = new ArrayList<>();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!validFields.contains(key)) {
                Constants.LOGGER.warn("[QueryBuilder] Ignorando campo invalido en WHERE: " + key);
                continue;
            }

            if (value.startsWith("(") && value.endsWith(")")) {
                conditions.add(key + " IN " + value);
            } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                conditions.add(key + " = " + value);
            } else {
                conditions.add(key + " = '" + value + "'");
            }
        }

        if (!conditions.isEmpty()) {
            query.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }

        return this;
    }

    public <T> QueryBuilder where(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        Set<String> validFields = entityClass != null
            ? Arrays.stream(entityClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet())
            : Collections.emptySet();

        List<String> conditions = new ArrayList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    String key = field.getName();
                    if (!validFields.contains(key)) {
                        Constants.LOGGER.warn("[QueryBuilder] Ignorando campo invalido en WHERE: " + key);
                        continue;
                    }
                    Object value = extractValue(fieldValue);
                    if (value instanceof String || value instanceof LocalDateTime) {
                        conditions.add(key + " = '" + value + "'");
                    } else {
                        conditions.add(key + " = " + value);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }

        if (!conditions.isEmpty()) {
            query.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }

        return this;
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
                    Object value = extractValue(fieldValue);
                    if (value instanceof String || value instanceof LocalDateTime) {
                        values.add("'" + value + "'");
                    } else {
                        values.add(value.toString());
                    }
                } else {
                    values.add("NULL");
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }
        qb.query.append(columns).append(") ");
        qb.query.append("VALUES (").append(values).append(") RETURNING * ");
        return qb;
    }

    public static <T> QueryBuilder update(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        QueryBuilder qb = new QueryBuilder();
        String table = getTableName(object.getClass());
        qb.query.append("UPDATE ").append(table).append(" SET ");

        StringJoiner setJoiner = new StringJoiner(", ");
        StringJoiner whereJoiner = new StringJoiner(" AND ");

        Field idField = null;

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (fieldValue == null) continue;

                String fieldName = field.getName();
                Object value = extractValue(fieldValue);

                if (fieldName.endsWith("_id")) {
                    idField = field;
                    whereJoiner.add(fieldName + " = " + (value instanceof String
                    		|| value instanceof LocalDateTime ? "'" + value + "'" : value));
                    continue;
                }

                setJoiner.add(fieldName + " = " + (value instanceof String
                		|| value instanceof LocalDateTime ? "'" + value + "'" : value));
            } catch (Exception e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }

        if (idField == null) {
            throw new IllegalArgumentException("No ID field (ending with _id) found for WHERE clause");
        }

        qb.query.append(setJoiner).append(" WHERE ").append(whereJoiner);
        return qb;
    }
    
    public static <T> QueryBuilder updateWithNulls(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        QueryBuilder qb = new QueryBuilder();
        String table = getTableName(object.getClass());
        qb.query.append("UPDATE ").append(table).append(" SET ");

        StringJoiner setJoiner = new StringJoiner(", ");
        StringJoiner whereJoiner = new StringJoiner(" AND ");

        Field idField = null;

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                Object fieldValue = field.get(object);

                if (fieldName.endsWith("_id")) {
                    idField = field;
                    Object value = extractValue(fieldValue);
                    whereJoiner.add(fieldName + " = " + (value instanceof String || value instanceof LocalDateTime ? "'" + value + "'" : value));
                    continue;
                }

                if (fieldValue == null) {
                    setJoiner.add(fieldName + " = NULL"); // ✅ esto lo borra en la BD
                } else {
                    Object value = extractValue(fieldValue);
                    setJoiner.add(fieldName + " = " + (value instanceof String || value instanceof LocalDateTime ? "'" + value + "'" : value));
                }
            } catch (Exception e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }

        if (idField == null) {
            throw new IllegalArgumentException("No ID field (ending with _id) found for WHERE clause");
        }

        qb.query.append(setJoiner).append(" WHERE ").append(whereJoiner);
        return qb;
    }


    public static <T> QueryBuilder delete(T object) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null");

        QueryBuilder qb = new QueryBuilder();
        String table = getTableName(object.getClass());
        qb.query.append("DELETE FROM ").append(table).append(" WHERE ");

        StringJoiner joiner = new StringJoiner(" AND ");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    Object value = extractValue(fieldValue);
                    joiner.add(field.getName() + " = " + (value instanceof String
                    		|| value instanceof LocalDateTime ? "'" + value + "'" : value.toString()));
                }
            } catch (Exception e) {
                Constants.LOGGER.error("(REFLECTION) Error reading field: " + e.getMessage());
            }
        }

        qb.query.append(joiner).append(" ");
        return qb;
    }

    public QueryBuilder orderBy(Optional<String> column, Optional<String> order) {
        column.ifPresent(c -> {
            if (entityClass != null) {
                boolean isValid = Arrays.stream(entityClass.getDeclaredFields())
                    .map(Field::getName)
                    .anyMatch(f -> f.equals(c));

                if (!isValid) {
                    Constants.LOGGER.warn("[QueryBuilder] Ignorando campo invalido en ORDER BY: " + c);
                    return;
                }
            }

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

    public QueryBuilder offset(Optional<Integer> offsetParam) {
        offsetParam.ifPresent(param -> limit += "OFFSET " + param + " ");
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