package org.cresplanex.core.common.jdbc.sqldialect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.sql.DataSource;

import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.jdbc.SchemaAndTable;

public interface CoreSqlDialect extends CoreSqlDialectOrder {

    boolean supports(String driver);

    boolean accepts(String name);

    String getCurrentTimeInMillisecondsExpression();

    String addLimitToSql(String sql, String limitExpression);

    String addReturningOfGeneratedIdToSql(String sql, String idColumn);

    default String castToJson(String sqlPart,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            BiFunction<String, List<Object>, List<Map<String, Object>>> selectCallback) {
        return sqlPart;
    }

    default String jsonColumnToString(Object object,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor) {
        return object.toString();
    }

    List<String> getPrimaryKeyColumns(DataSource dataSource, String dataSourceUrl, SchemaAndTable schemaAndTable) throws SQLException;
}
