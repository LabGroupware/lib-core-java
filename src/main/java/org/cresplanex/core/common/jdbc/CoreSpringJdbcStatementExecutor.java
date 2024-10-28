package org.cresplanex.core.common.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Springの {@link JdbcTemplate} を使用してJDBCステートメントを実行するクラス。
 * <p>
 * SQLクエリの実行、データ挿入時の生成ID取得、更新、リスト形式でのクエリ取得などの機能を提供します。
 * </p>
 */
public class CoreSpringJdbcStatementExecutor implements CoreJdbcStatementExecutor {

    /**
     * JDBC操作に使用する {@link JdbcTemplate} のインスタンス
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * ログを出力するためのロガー
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@link CoreSpringJdbcStatementExecutor} のコンストラクタ。
     *
     * @param jdbcTemplate SpringのJdbcTemplateインスタンス
     */
    public CoreSpringJdbcStatementExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * データを挿入し、生成されたIDを返します。
     *
     * @param sql 実行するSQLクエリ
     * @param idColumn 生成されたIDのカラム名
     * @param params クエリに渡すパラメータ
     * @return 生成されたIDの値
     * @throws CoreDuplicateKeyException 重複キーが発生した場合
     */
    @Override
    public long insertAndReturnGeneratedId(String sql, String idColumn, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertAndReturnGeneratedId {} {} {}", sql, idColumn, Arrays.asList(params));
        }
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i, params[i - 1]);
                }

                return preparedStatement;
            }, holder);

            Map<String, Object> keys = holder.getKeys();

            if (keys == null) {
                throw new IllegalStateException("Generated keys are null");
            }

            if (keys.size() > 1) {
                if (keys.containsKey(idColumn)) {
                    return (Long) keys.get(idColumn);
                } else {
                    throw new IllegalStateException("Generated keys are null or do not contain the specified id column");
                }
            } else {
                Number key = holder.getKey();
                if (key != null) {
                    return key.longValue();
                } else {
                    throw new IllegalStateException("Generated key is null");
                }
            }
        } catch (DuplicateKeyException e) {
            throw new CoreDuplicateKeyException(e);
        }
    }

    /**
     * データを更新します。
     *
     * @param sql 実行するSQLクエリ
     * @param params クエリに渡すパラメータ
     * @return 更新された行数
     * @throws CoreDuplicateKeyException 重複キーが発生した場合
     */
    @Override
    public int update(String sql, Object... params) {
        try {
            return jdbcTemplate.update(sql, params);
        } catch (DuplicateKeyException e) {
            throw new CoreDuplicateKeyException(e);
        }
    }

    /**
     * クエリを実行してリストを取得します。
     *
     * @param <T> クエリ結果の型
     * @param sql 実行するSQLクエリ
     * @param coreRowMapper 結果をマッピングするローマッパー
     * @param params クエリに渡すパラメータ
     * @return クエリ結果のリスト
     */
    @Override
    public <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... params) {
        return jdbcTemplate.query(sql, coreRowMapper::mapRow, params);
    }

    /**
     * クエリを実行してマップ形式でリストを取得します。
     *
     * @param sql 実行するSQLクエリ
     * @param parameters クエリに渡すパラメータ
     * @return クエリ結果のリスト（各行をマップ形式で保持）
     */
    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... parameters) {
        return jdbcTemplate.queryForList(sql, parameters);
    }
}
