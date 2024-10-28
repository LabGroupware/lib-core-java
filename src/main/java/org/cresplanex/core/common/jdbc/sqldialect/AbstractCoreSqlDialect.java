package org.cresplanex.core.common.jdbc.sqldialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.NotImplementedException;
import org.cresplanex.core.common.jdbc.SchemaAndTable;

/**
 * SQL方言の共通の基底クラス。
 * <p>
 * 特定のSQLドライバやデータベースの名前に対応する実装を提供し、SQL文への制限句や生成IDの取得を行う メソッドなど、共通のSQL操作を実装しています。
 * </p>
 */
public abstract class AbstractCoreSqlDialect implements CoreSqlDialect {

    /**
     * 対応するSQLドライバのセット
     */
    private final Set<String> drivers;

    /**
     * 対応するデータベース名のセット
     */
    private final Set<String> names;

    /**
     * 現在の時間（ミリ秒）を取得するためのカスタム式
     */
    private final String customCurrentTimeInMillisecondsExpression;

    /**
     * コンストラクタ。ドライバ名、データベース名、現在の時間取得式を指定してインスタンスを生成します。
     *
     * @param drivers 対応するドライバ名のセット
     * @param names 対応するデータベース名のセット
     * @param customCurrentTimeInMillisecondsExpression 現在の時間を取得するSQL式
     */
    public AbstractCoreSqlDialect(Set<String> drivers, Set<String> names, String customCurrentTimeInMillisecondsExpression) {
        this.drivers = drivers;
        this.names = names;
        this.customCurrentTimeInMillisecondsExpression = customCurrentTimeInMillisecondsExpression;
    }

    /**
     * 指定されたドライバがサポートされているかを確認します。
     *
     * @param driver 確認するドライバ名
     * @return サポートされている場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean supports(String driver) {
        return drivers.contains(driver);
    }

    /**
     * 指定されたデータベース名が受け入れられるかを確認します。
     *
     * @param name 確認するデータベース名
     * @return 受け入れ可能な場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean accepts(String name) {
        return names.contains(name);
    }

    /**
     * SQL文に制限句（LIMIT）を追加します。
     *
     * @param sql 元のSQL文
     * @param limitExpression 制限句として使用する表現
     * @return 制限句が追加されたSQL文
     */
    @Override
    public String addLimitToSql(String sql, String limitExpression) {
        return String.format("%s limit %s", sql, limitExpression);
    }

    /**
     * 生成IDを返すSQL文を追加します。このメソッドはサブクラスでの実装が必要です。
     *
     * @param sql 元のSQL文
     * @param idColumn 生成IDを返すカラム名
     * @throws NotImplementedException 実装が提供されていない場合にスローされます
     */
    @Override
    public String addReturningOfGeneratedIdToSql(String sql, String idColumn) {
        throw new NotImplementedException();
    }

    /**
     * 現在の時間（ミリ秒）を取得するSQL式を返します。
     *
     * @return 現在の時間を取得するカスタムSQL式
     */
    @Override
    public String getCurrentTimeInMillisecondsExpression() {
        return customCurrentTimeInMillisecondsExpression;
    }

    /**
     * 指定されたテーブルの主キー列のリストを取得します。
     *
     * @param dataSource データソース
     * @param dataSourceUrl データソースのURL
     * @param schemaAndTable スキーマとテーブル名
     * @return 主キー列名のリスト
     * @throws SQLException データベースアクセスエラーが発生した場合
     */
    @Override
    public List<String> getPrimaryKeyColumns(DataSource dataSource, String dataSourceUrl, SchemaAndTable schemaAndTable) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection
                    .getMetaData()
                    .getPrimaryKeys(getJdbcCatalogue(dataSourceUrl),
                            schemaAndTable.getSchema(),
                            schemaAndTable.getTableName());

            List<String> pkColumns = new ArrayList<>();

            while (resultSet.next()) {
                pkColumns.add(resultSet.getString("COLUMN_NAME"));
            }

            return pkColumns;
        }
    }

    /**
     * デフォルトのJDBCカタログを取得します。このメソッドはサブクラスでオーバーライド可能です。
     *
     * @param dataSourceUrl データソースのURL
     * @return JDBCカタログ
     */
    protected String getJdbcCatalogue(String dataSourceUrl) {
        return null;
    }

    /**
     * SQL方言の順序を返します。
     *
     * @return 順序値
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
