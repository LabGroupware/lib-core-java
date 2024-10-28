package org.cresplanex.core.common.jdbc;

import org.cresplanex.core.common.jdbc.sqldialect.CoreSqlDialect;

/**
 * JDBC操作のユーティリティクラス。
 * <p>
 * データベースのイベントテーブルやメッセージテーブルにデータを挿入するSQL文を生成します。また、
 * ブール値を整数に変換するユーティリティメソッドも提供します。
 * </p>
 */
public class CoreJdbcOperationsUtils {

    /**
     * 自動生成されたIDを持つメッセージのカラム名
     */
    public static final String MESSAGE_AUTO_GENERATED_ID_COLUMN = "dbid";

    /**
     * 自動生成されたIDを持つイベントのカラム名
     */
    public static final String EVENT_AUTO_GENERATED_ID_COLUMN = "id";

    /**
     * アプリケーションが生成したメッセージIDのカラム名
     */
    public static final String MESSAGE_APPLICATION_GENERATED_ID_COLUMN = "id";

    /**
     * アプリケーションが生成したイベントIDのカラム名
     */
    public static final String EVENT_APPLICATION_GENERATED_ID_COLUMN = "event_id";

    /**
     * SQL方言（SQLの構文や関数に依存した表現）を扱うためのオブジェクト
     */
    private final CoreSqlDialect coreSqlDialect;

    /**
     * コンストラクタ。指定されたSQL方言でインスタンスを構築します。
     *
     * @param coreSqlDialect SQL方言のインスタンス
     */
    public CoreJdbcOperationsUtils(CoreSqlDialect coreSqlDialect) {
        this.coreSqlDialect = coreSqlDialect;
    }

    /**
     * アプリケーションが生成したイベントIDを持つイベントテーブルへの挿入SQLを生成します。
     *
     * @param coreSchema 使用するスキーマ
     * @return イベントテーブルへの挿入SQL
     */
    public String insertIntoEventsTableApplicationIdSql(CoreSchema coreSchema) {
        return String.format("INSERT INTO %s (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)", coreSchema.qualifyTable("events"));
    }

    /**
     * データベースが生成したイベントIDを持つイベントテーブルへの挿入SQLを生成します。
     *
     * @param coreSchema 使用するスキーマ
     * @return イベントテーブルへの挿入SQL
     */
    public String insertIntoEventsTableDbIdSql(CoreSchema coreSchema) {
        return String.format("INSERT INTO %s (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)"
                + " VALUES ('', ?, ?, ?, ?, ?, ?, ?)", coreSchema.qualifyTable("events"));
    }

    /**
     * アプリケーションが生成したメッセージIDを持つメッセージテーブルへの挿入SQLを生成します。
     *
     * @param coreSchema 使用するスキーマ
     * @param jsonConverter JSON形式に変換するコンバータ
     * @param outboxTableSuffix アウトボックステーブルのサフィックス
     * @return メッセージテーブルへの挿入SQL
     */
    public String insertIntoMessageTableApplicationIdSql(CoreSchema coreSchema, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
        return insertIntoMessageTable(coreSchema,
                "insert into %s%s(id, destination, headers, payload, creation_time, published, message_partition) values(?, ?, %s, %s, %s, ?, ?)",
                jsonConverter, outboxTableSuffix);
    }

    /**
     * データベースが生成したメッセージIDを持つメッセージテーブルへの挿入SQLを生成します。
     *
     * @param coreSchema 使用するスキーマ
     * @param jsonConverter JSON形式に変換するコンバータ
     * @param outboxTableSuffix アウトボックステーブルのサフィックス
     * @return メッセージテーブルへの挿入SQL
     */
    public String insertIntoMessageTableDbIdSql(CoreSchema coreSchema, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
        return insertIntoMessageTable(coreSchema,
                "insert into %s%s(id, destination, headers, payload, creation_time, published, message_partition) values('', ?, %s, %s, %s, ?, ?)",
                jsonConverter, outboxTableSuffix);
    }

    /**
     * メッセージテーブルへの挿入SQLを生成します。
     *
     * @param coreSchema 使用するスキーマ
     * @param sql 挿入SQLのテンプレート
     * @param jsonConverter JSON形式に変換するコンバータ
     * @param outboxTableSuffix アウトボックステーブルのサフィックス
     * @return メッセージテーブルへの挿入SQL
     */
    public String insertIntoMessageTable(CoreSchema coreSchema, String sql, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
        return String.format(sql,
                coreSchema.qualifyTable("message"),
                outboxTableSuffix,
                jsonConverter.convert(coreSchema, "headers"),
                jsonConverter.convert(coreSchema, "payload"),
                coreSqlDialect.getCurrentTimeInMillisecondsExpression());
    }

    /**
     * ブール値を整数（1または0）に変換します。
     *
     * @param bool 変換するブール値
     * @return trueの場合は1、falseの場合は0
     */
    public int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }
}
