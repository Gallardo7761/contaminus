package net.miarma.contaminus.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class QueryBuilder {
    private StringBuilder query;
    private List<String> conditions;
    
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
    
    public QueryBuilder from(String table) {
        query.append("FROM ").append(table).append(" ");
        return this;
    }
    
    public QueryBuilder where(String condition) {
        conditions.add(condition);
        return this;
    }
    
    public QueryBuilder orderBy(Optional<String> column, Optional<String> order) {
        if (column.isPresent()) {
            query.append("ORDER BY ").append(column.get()).append(" ");
            if (order.isPresent()) {
                query.append(order.get().equalsIgnoreCase("asc") ? "ASC" : "DESC").append(" ");
            }
        }
        return this;
    }
    
    public QueryBuilder limit(Optional<Integer> limit) {
        limit.ifPresent(value -> query.append("LIMIT ").append(value).append(" "));
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
        return query.toString().trim() + ";";
    }
}