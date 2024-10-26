package org.cresplanex.core.common.jdbc;

import java.util.List;
import java.util.Map;

public interface CoreJdbcStatementExecutor {

    long insertAndReturnGeneratedId(String sql, String idColumn, Object... params);

    int update(String sql, Object... params);

    <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... params);

    List<Map<String, Object>> queryForList(String sql, Object... parameters);
}
