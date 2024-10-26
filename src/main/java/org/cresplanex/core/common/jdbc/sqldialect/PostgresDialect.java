package org.cresplanex.core.common.jdbc.sqldialect;

import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.postgresql.util.PGobject;

public class PostgresDialect extends AbstractCoreSqlDialect {

    private final ConcurrentMap<ColumnCacheKey, String> columnTypeCache = new ConcurrentHashMap<>();

    public PostgresDialect() {
        super(Collections.singleton("org.postgresql.Driver"),
                Collections.unmodifiableSet(new HashSet<>(asList("postgres", "postgresql", "pgsql", "pg"))),
                "(ROUND(EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000))");
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String castToJson(String sqlPart,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            BiFunction<String, List<Object>, List<Map<String, Object>>> selectCallback) {

        String columnType = getColumnType(coreSchema, unqualifiedTable, column, selectCallback);

        return String.format("%s::%s", sqlPart, columnType);
    }

    @Override
    public String jsonColumnToString(Object object,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor) {

        if (object instanceof String string) {
            return string;
        }

        if (object instanceof PGobject pGobject) {

            if ("json".equals(pGobject.getType())) {
                return pGobject.getValue();
            }

            throw new IllegalArgumentException(String.format("Unsupported postgres type %s of column %s", pGobject.getType(), column));
        }

        if (object == null) {
            throw new IllegalArgumentException(String.format("Object is null for column %s", column));
        }
        throw new IllegalArgumentException(String.format("Unsupported java type %s for column %s", object.getClass(), column));
        // throw new IllegalArgumentException(String.format("Unsupported java type %s for column %s", object.getClass(), column));
    }

    public String getColumnType(CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            BiFunction<String, List<Object>, List<Map<String, Object>>> selectCallback) {

        return columnTypeCache.computeIfAbsent(
                new ColumnCacheKey(coreSchema.getCoreDatabaseSchema(), unqualifiedTable, column),
                columnCacheKey -> {
                    final String sql
                    = "select data_type "
                    + "from information_schema.columns "
                    + "where table_schema = ? and table_name = ? and column_name = ?";

                    List<Object> queryArgs = asList(coreSchema.isEmpty() ? "public" : coreSchema.getCoreDatabaseSchema(), unqualifiedTable, column);
                    List<Map<String, Object>> results = selectCallback.apply(sql, queryArgs);
                    if (results.isEmpty()) {
                        throw new RuntimeException("Could not retrieve metadata for " + queryArgs);
                    }
                    return (String) results.get(0).get("data_type");
                });
    }

    @Override
    public String addReturningOfGeneratedIdToSql(String sql, String idColumn) {
        return String.format("%s RETURNING %s", sql, idColumn);
    }
}
