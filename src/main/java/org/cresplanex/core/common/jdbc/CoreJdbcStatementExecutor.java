package org.cresplanex.core.common.jdbc;

import java.util.List;
import java.util.Map;

/**
 * JDBCステートメントを実行するためのインターフェース。
 * <p>
 * SQLクエリを実行して、データの挿入、更新、クエリ操作を行うメソッドを提供します。
 * </p>
 */
public interface CoreJdbcStatementExecutor {

    /**
     * データを挿入し、生成されたIDを返します。
     *
     * @param sql 実行するSQLクエリ
     * @param idColumn 生成されるIDのカラム名
     * @param params クエリに渡すパラメータ
     * @return 生成されたIDの値
     */
    long insertAndReturnGeneratedId(String sql, String idColumn, Object... params);

    /**
     * データを更新します。
     *
     * @param sql 実行するSQLクエリ
     * @param params クエリに渡すパラメータ
     * @return 更新された行数
     */
    int update(String sql, Object... params);

    /**
     * クエリを実行してリストを取得します。
     *
     * @param <T> クエリ結果の型
     * @param sql 実行するSQLクエリ
     * @param coreRowMapper 結果をマッピングするローマッパー(取得したレコードを基にマッピングしたオブジェクトを返す)
     * @param params クエリに渡すパラメータ
     * @return クエリ結果のリスト
     */
    <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... params);

    /**
     * クエリを実行してマップ形式でリストを取得します。
     *
     * @param sql 実行するSQLクエリ
     * @param parameters クエリに渡すパラメータ
     * @return クエリ結果のリスト（各行をマップ形式で保持）
     */
    List<Map<String, Object>> queryForList(String sql, Object... parameters);
}
