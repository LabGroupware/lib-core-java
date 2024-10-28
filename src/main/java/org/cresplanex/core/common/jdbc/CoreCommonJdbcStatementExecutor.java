package org.cresplanex.core.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * JDBCステートメントの実行を行うクラス。
 * <p>
 * SQLクエリの実行、データ挿入時の生成ID取得、更新、リスト形式でのクエリ取得などの機能を提供します。 重複キーエラーのハンドリングも実装されています。
 * Springを使用するため, 今回は利用しません.
 * </p>
 */
public class CoreCommonJdbcStatementExecutor implements CoreJdbcStatementExecutor {

    /**
     * 重複キーエラーと判断されるSQLエラーコードのセット
     */
    private static final Set<Integer> DUPLICATE_KEY_ERROR_CODES = new HashSet<>(Arrays.asList(
            1062, // MySQL
            2601, 2627, // MS-SQL
            23505, // Postgres
            23001 // H2
    ));

    /**
     * 接続を提供するための {@link Supplier} インターフェース
     */
    private final Supplier<Connection> connectionProvider;

    /**
     * コンストラクタ。指定された接続プロバイダーでインスタンスを構築します。
     *
     * @param connectionProvider データベース接続を提供する {@link Supplier} インターフェース
     */
    public CoreCommonJdbcStatementExecutor(Supplier<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * データを挿入し、生成されたIDを返します。
     *
     * @param sql 実行するSQLクエリ
     * @param idColumn 生成されたIDのカラム名
     * @param parameters クエリに渡すパラメータ
     * @return 生成されたIDの値
     * @throws CoreDuplicateKeyException 重複キーエラーが発生した場合
     */
    @Override
    public long insertAndReturnGeneratedId(String sql, String idColumn, Object... parameters) {
        Connection connection = connectionProvider.get();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 1; i <= parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i - 1]);
            }

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    if (generatedKeys.getMetaData().getColumnCount() == 1) {
                        return generatedKeys.getLong(1);
                    }
                    return generatedKeys.getLong(idColumn);
                } else {
                    throw new CoreSqlException("Id was not generated");
                }
            }
        } catch (SQLException e) {
            handleSqlUpdateException(e);
            return -1;
        }
    }

    /**
     * データを更新します。
     *
     * @param sql 実行するSQLクエリ
     * @param parameters クエリに渡すパラメータ
     * @return 更新された行数
     * @throws CoreDuplicateKeyException 重複キーエラーが発生した場合
     */
    @Override
    public int update(String sql, Object... parameters) {
        Connection connection = connectionProvider.get();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i - 1]);
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSqlUpdateException(e);
            return 0;
        }
    }

    /**
     * クエリを実行してリストを取得します。
     *
     * @param <T> クエリ結果の型
     * @param sql 実行するSQLクエリ
     * @param coreRowMapper 結果をマッピングするローマッパー
     * @param parameters クエリに渡すパラメータ
     * @return クエリ結果のリスト
     */
    @Override
    public <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... parameters) {
        Connection connection = connectionProvider.get();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i - 1]);
            }

            ResultSet rs = preparedStatement.executeQuery();

            List<T> result = new ArrayList<>();

            int rowNum = 0;
            while (rs.next()) {
                result.add(coreRowMapper.mapRow(rs, rowNum++));
            }

            return result;
        } catch (SQLException e) {
            throw new CoreSqlException(e);
        }
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
        Connection connection = connectionProvider.get();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i - 1]);
            }

            ResultSet rs = preparedStatement.executeQuery();

            List<Map<String, Object>> result = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                result.add(row);
            }

            return result;
        } catch (SQLException e) {
            throw new CoreSqlException(e);
        }
    }

    /**
     * SQLの更新例外を処理します。重複キーエラーの場合は {@link CoreDuplicateKeyException} を投げます。
     *
     * @param e 発生した {@link SQLException}
     */
    private void handleSqlUpdateException(SQLException e) {
        Optional<Integer> additionalErrorCode = Optional.empty();

        try {
            additionalErrorCode = Optional.of(Integer.valueOf(e.getSQLState()));
        } catch (NumberFormatException nfe) {
            // 無視
        }

        if (DUPLICATE_KEY_ERROR_CODES.contains(e.getErrorCode())
                || additionalErrorCode.map(DUPLICATE_KEY_ERROR_CODES::contains).orElse(false)) {
            throw new CoreDuplicateKeyException(e);
        }

        throw new CoreSqlException(e);
    }
}
