package net.miarma.contaminus.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class QueryBuilder {
    private StringBuilder query;
    private List<String> conditions;
    private String sort;
    private String order;
    private String limit;
    
    public QueryBuilder() {
        this.query = new StringBuilder();
        this.conditions = new ArrayList<>();
    }
    
    public static QueryBuilder select(String... columns) {
        QueryBuilder qb = new QueryBuilder();
        StringJoiner joiner = new StringJoiner(", ");
        for (String column : columns) {
            joiner.add(column);
        }
        qb.query.append("SELECT ").append(joiner).append(" ");
        return qb;
    }
    
    public static QueryBuilder insert(String table, String... columns) {
		QueryBuilder qb = new QueryBuilder();
		StringJoiner joiner = new StringJoiner(", ");
		if (columns.length > 0) {
			for (String column : columns) {
				joiner.add(column);
			}
			qb.query.append("INSERT INTO ").append(table).append(" (").append(joiner).append(") ");
		} else {
			qb.query.append("INSERT INTO ").append(table).append(" ");
		}
		
		return qb;
	}
    
    public static QueryBuilder update(String table) {
		QueryBuilder qb = new QueryBuilder();
		qb.query.append("UPDATE ").append(table).append(" ");
		return qb;
	}
    
    public QueryBuilder set(String column, Object value) {
		if(value instanceof String) {
			query.append("SET ").append(column).append(" = '").append(value).append("' ");
		} else {
			query.append("SET ").append(column).append(" = ").append(value).append(" ");
		}
		return this;
	}
    
    public QueryBuilder values(Object... values) {
        StringJoiner joiner = new StringJoiner(", ", "VALUES (", ")");
        for (Object value : values) {
            if(value instanceof String) {
                joiner.add("'" + value + "'");
            } else if(value == null) {
				joiner.add("NULL");
			}
            else {
                joiner.add(value.toString());
            }
        }
        this.query.append(joiner).append(" ");
        return this;
    }
    
    public QueryBuilder from(String table) {
        query.append("FROM ").append(table).append(" ");
        return this;
    }
    
    public QueryBuilder where(String conditionsString, Object... values) {
    	conditionsString = conditionsString.replaceAll("\\?", "%s");
        conditions.add(String.format(conditionsString, values));
        return this;
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
        if (!conditions.isEmpty()) {
            query.append("WHERE ");
            StringJoiner joiner = new StringJoiner(" AND ");
            for (String condition : conditions) {
                joiner.add(condition);
            }
            query.append(joiner).append(" ");
        }
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
