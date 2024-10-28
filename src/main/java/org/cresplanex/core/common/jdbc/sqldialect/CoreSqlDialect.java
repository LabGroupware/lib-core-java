package org.cresplanex.core.common.jdbc.sqldialect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.sql.DataSource;

import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.jdbc.SchemaAndTable;

/**
 * SQL方言を定義するインターフェース。
 * <p>
 * データベースごとに異なるSQL構文や機能を統一して操作できるようにするためのメソッドを提供します。
 * 各SQLドライバやデータベース名のサポート、JSONのキャストやIDの取得、プライマリキーの取得などに対応します。
 * </p>
 */
public interface CoreSqlDialect extends CoreSqlDialectOrder {

    /**
     * 指定されたドライバがサポートされているかを確認します。
     *
     * @param driver 確認するドライバ名
     * @return サポートされている場合はtrue、そうでない場合はfalse
     */
    boolean supports(String driver);

    /**
     * 指定されたデータベース名が受け入れられるかを確認します。
     *
     * @param name 確認するデータベース名
     * @return 受け入れ可能な場合はtrue、そうでない場合はfalse
     */
    boolean accepts(String name);

    /**
     * 現在の時間（ミリ秒）を取得するSQL式を返します。
     *
     * @return 現在の時間を取得するカスタムSQL式
     */
    String getCurrentTimeInMillisecondsExpression();

    /**
     * SQL文に制限句（LIMIT）を追加します。
     *
     * @param sql 元のSQL文
     * @param limitExpression 制限句として使用する表現
     * @return 制限句が追加されたSQL文
     */
    String addLimitToSql(String sql, String limitExpression);

    /**
     * 生成IDを返すSQL文を追加します。
     *
     * @param sql 元のSQL文
     * @param idColumn 生成IDを返すカラム名
     * @return 生成IDを返すSQL文
     */
    String addReturningOfGeneratedIdToSql(String sql, String idColumn);

    /**
     * SQL部分をJSON形式にキャストします。
     *
     * @param sqlPart JSONに変換するSQL部分
     * @param coreSchema スキーマ情報
     * @param unqualifiedTable テーブル名
     * @param column カラム名
     * @param selectCallback SQL選択コールバック
     * @return JSON形式にキャストされたSQL部分
     */
    default String castToJson(String sqlPart,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            BiFunction<String, List<Object>, List<Map<String, Object>>> selectCallback) {
        return sqlPart;
    }

    /**
     * JSONカラムを文字列に変換します。
     *
     * @param object JSONカラムを含むオブジェクト
     * @param coreSchema スキーマ情報
     * @param unqualifiedTable テーブル名
     * @param column カラム名
     * @param coreJdbcStatementExecutor ステートメント実行オブジェクト
     * @return JSONカラムを文字列として変換した結果
     */
    default String jsonColumnToString(Object object,
            CoreSchema coreSchema,
            String unqualifiedTable,
            String column,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor) {
        return object.toString();
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
    List<String> getPrimaryKeyColumns(DataSource dataSource, String dataSourceUrl, SchemaAndTable schemaAndTable) throws SQLException;
}
