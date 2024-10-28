package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collections;
import java.util.HashSet;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.postgresql.util.PGobject;

/**
 * PostgreSQL用のSQL方言クラス。
 * <p>
 * PostgreSQLのドライバとデータベース名に対応し、JSONのキャスト、IDの取得、およびカラムタイプのキャッシュ機能を提供します。
 * </p>
 */
public class PostgresDialect extends AbstractCoreSqlDialect {

    private final ConcurrentMap<ColumnCacheKey, String> columnTypeCache = new ConcurrentHashMap<>();

    /**
     * コンストラクタ。PostgreSQL用の設定を行います。
     */
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

            throw new IllegalArgumentException(String.format("Unsupported Postgres type: %s Column: %s", pGobject.getType(), column));
        }

        if (object == null) {
            throw new IllegalArgumentException(String.format("Column %s object is null", column));
        }
        throw new IllegalArgumentException(String.format("Unsupported Java type %s Column: %s", object.getClass(), column));
    }

    /**
     * 指定されたカラムのデータ型を取得します。カラムのデータ型はキャッシュされ、再利用されます。
     *
     * @param coreSchema スキーマ情報
     * @param unqualifiedTable テーブル名
     * @param column カラム名
     * @param selectCallback SQL選択コールバック
     * @return カラムのデータ型
     */
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
                        throw new RuntimeException("Cannot get metadata: " + queryArgs);
                    }
                    return (String) results.get(0).get("data_type");
                });
    }

    @Override
    public String addReturningOfGeneratedIdToSql(String sql, String idColumn) {
        return String.format("%s RETURNING %s", sql, idColumn);
    }
}
