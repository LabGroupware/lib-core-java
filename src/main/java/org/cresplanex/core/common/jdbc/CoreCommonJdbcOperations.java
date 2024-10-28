package org.cresplanex.core.common.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.common.id.IdGenerator;
import static org.cresplanex.core.common.jdbc.CoreJdbcOperationsUtils.EVENT_AUTO_GENERATED_ID_COLUMN;
import static org.cresplanex.core.common.jdbc.CoreJdbcOperationsUtils.MESSAGE_AUTO_GENERATED_ID_COLUMN;
import org.cresplanex.core.common.jdbc.sqldialect.CoreSqlDialect;
import org.cresplanex.core.common.json.mapper.JSonMapper;

/**
 * JDBCを使用してイベントおよびメッセージの挿入操作を行うユーティリティクラス。
 * <p>
 * アウトボックスメッセージとイベントのテーブルにデータを挿入する機能を提供します。各種挿入操作に対して、アプリケーションまたは
 * データベースが生成するIDをサポートしています。
 * </p>
 */
public class CoreCommonJdbcOperations {

    private final CoreJdbcOperationsUtils coreJdbcOperationsUtils;
    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final OutboxPartitioningSpec outboxPartitioningSpec;
    private final CoreSqlDialect coreSqlDialect;

    /**
     * コンストラクタ。指定されたコンポーネントでインスタンスを構築します。
     *
     * @param coreJdbcOperationsUtils JDBC操作ユーティリティ
     * @param coreJdbcStatementExecutor ステートメント実行オブジェクト
     * @param coreSqlDialect SQL方言
     * @param outboxPartitioningSpec アウトボックスパーティショニング仕様
     */
    public CoreCommonJdbcOperations(CoreJdbcOperationsUtils coreJdbcOperationsUtils,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSqlDialect coreSqlDialect,
            OutboxPartitioningSpec outboxPartitioningSpec) {
        this.coreJdbcOperationsUtils = coreJdbcOperationsUtils;
        this.coreSqlDialect = coreSqlDialect;
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        this.outboxPartitioningSpec = outboxPartitioningSpec;
    }

    /**
     * 現在のSQLデータベースMSを取得します。
     *
     * @return 現在の {@link CoreSqlDialect} インスタンス
     */
    public CoreSqlDialect getCoreSqlDialect() {
        return coreSqlDialect;
    }

    /**
     * イベントテーブルにデータを挿入するSQLを生成します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param entityId エンティティID
     * @param eventData イベントデータ
     * @param eventType イベントタイプ
     * @param entityType エンティティタイプ
     * @param triggeringEvent トリガーとなるイベント
     * @param metadata メタデータ
     * @param coreSchema 使用するスキーマ
     * @return 生成されたイベントID
     */
    public String insertIntoEventsTable(IdGenerator idGenerator,
            String entityId,
            String eventData,
            String eventType,
            String entityType,
            Optional<String> triggeringEvent,
            Optional<String> metadata,
            CoreSchema coreSchema) {

        return insertIntoEventsTable(idGenerator,
                entityId, eventData, eventType, entityType, triggeringEvent, metadata, coreSchema, false);
    }

    /**
     * 発行済みのイベントをイベントテーブルに挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param entityId エンティティID
     * @param eventData イベントデータ
     * @param eventType イベントタイプ
     * @param entityType エンティティタイプ
     * @param triggeringEvent トリガーとなるイベント
     * @param metadata メタデータ
     * @param coreSchema 使用するスキーマ
     * @return 生成されたイベントID
     */
    public String insertPublishedEventIntoEventsTable(IdGenerator idGenerator,
            String entityId,
            String eventData,
            String eventType,
            String entityType,
            Optional<String> triggeringEvent,
            Optional<String> metadata,
            CoreSchema coreSchema) {

        return insertIntoEventsTable(idGenerator,
                entityId, eventData, eventType, entityType, triggeringEvent, metadata, coreSchema, true);
    }

    /**
     * イベントテーブルにデータを挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param entityId エンティティID
     * @param eventData イベントデータ
     * @param eventType イベントタイプ
     * @param entityType エンティティタイプ
     * @param triggeringEvent トリガーとなるイベント
     * @param metadata メタデータ
     * @param coreSchema 使用するスキーマ
     * @param published イベントが発行済みかどうか
     * @return
     */
    private String insertIntoEventsTable(IdGenerator idGenerator,
            String entityId,
            String eventData,
            String eventType,
            String entityType,
            Optional<String> triggeringEvent,
            Optional<String> metadata,
            CoreSchema coreSchema,
            boolean published) {

        if (idGenerator.databaseIdRequired()) {
            Long databaseId = coreJdbcStatementExecutor
                    .insertAndReturnGeneratedId(coreJdbcOperationsUtils.insertIntoEventsTableDbIdSql(coreSchema),
                            EVENT_AUTO_GENERATED_ID_COLUMN,
                            eventType,
                            eventData,
                            entityType,
                            entityId,
                            triggeringEvent.orElse(null),
                            metadata.orElse(null),
                            coreJdbcOperationsUtils.booleanToInt(published));

            return idGenerator.genId(databaseId, null).asString();
        } else {
            String eventId = idGenerator.genId().asString();

            coreJdbcStatementExecutor
                    .update(coreJdbcOperationsUtils.insertIntoEventsTableApplicationIdSql(coreSchema),
                            eventId,
                            eventType,
                            eventData,
                            entityType,
                            entityId,
                            triggeringEvent.orElse(null),
                            metadata.orElse(null),
                            coreJdbcOperationsUtils.booleanToInt(published));

            return eventId;
        }
    }

    /**
     * アウトボックスメッセージテーブルにデータを挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param payload メッセージのペイロード
     * @param destination メッセージの宛先
     * @param headers メッセージヘッダー
     * @param coreSchema 使用するスキーマ
     * @return 生成されたメッセージID
     */
    public String insertIntoMessageTable(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            CoreSchema coreSchema) {

        return insertIntoMessageTable(idGenerator, payload, destination, headers, coreSchema, false);
    }

    /**
     * 発行済みのメッセージをアウトボックスメッセージテーブルに挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param payload メッセージのペイロード
     * @param destination メッセージの宛先
     * @param headers メッセージヘッダー
     * @param coreSchema 使用するスキーマ
     * @return 生成されたメッセージID
     */
    public String insertPublishedMessageIntoMessageTable(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            CoreSchema coreSchema) {

        return insertIntoMessageTable(idGenerator, payload, destination, headers, coreSchema, true);
    }

    /**
     * アウトボックスメッセージテーブルにデータを挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param payload メッセージのペイロード
     * @param destination メッセージの宛先
     * @param headers メッセージヘッダー
     * @param coreSchema 使用するスキーマ
     * @param published メッセージが発行済みかどうか
     * @return 生成されたメッセージID
     */
    private String insertIntoMessageTable(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            CoreSchema coreSchema,
            boolean published) {

        String messageKey = headers.get("PARTITION_ID");
        OutboxPartitionValues outboxPartitionValues = outboxPartitioningSpec.outboxTableValues(destination, messageKey);

        if (idGenerator.databaseIdRequired()) {
            return insertIntoMessageTableDatabaseId(idGenerator, payload, destination, headers, published, coreSchema, outboxPartitionValues);
        } else {
            return insertIntoMessageTableApplicationId(idGenerator, payload, destination, headers, published, coreSchema, outboxPartitionValues);
        }
    }

    /**
     * アウトボックスメッセージテーブルにデータを挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param payload メッセージのペイロード
     * @param destination メッセージの宛先
     * @param headers メッセージヘッダー
     * @param published メッセージが発行済みかどうか
     * @param coreSchema 使用するスキーマ
     * @param outboxPartitionValues アウトボックスパーティション値
     * @return 生成されたメッセージID
     */
    private String insertIntoMessageTableApplicationId(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            boolean published,
            CoreSchema coreSchema, OutboxPartitionValues outboxPartitionValues) {

        headers = new HashMap<>(headers);

        String messageId = idGenerator.genId(null, outboxPartitionValues.outboxTableSuffix.suffix).asString();

        verifyNoID(headers);

        headers.put("ID", messageId);

        String serializedHeaders = JSonMapper.toJson(headers);

        coreJdbcStatementExecutor.update(coreJdbcOperationsUtils.insertIntoMessageTableApplicationIdSql(coreSchema, this::columnToJson, outboxPartitionValues.outboxTableSuffix.suffixAsString),
                messageId, destination, serializedHeaders, payload, coreJdbcOperationsUtils.booleanToInt(published), outboxPartitionValues.messagePartition);

        return messageId;
    }

    /**
     * ヘッダーがIDを持たないことを検証します。
     *
     * @param headers メッセージヘッダー
     * @throws RuntimeException すでにIDが設定されている場合
     */
    private void verifyNoID(Map<String, String> headers) {
        if (headers.containsKey("ID")) {
            throw new RuntimeException("ID should not be already set");
        }
    }

    /**
     * アウトボックスメッセージテーブルにデータを挿入します。
     *
     * @param idGenerator ID生成オブジェクト
     * @param payload メッセージのペイロード
     * @param destination メッセージの宛先
     * @param headers メッセージヘッダー
     * @param published メッセージが発行済みかどうか
     * @param coreSchema 使用するスキーマ
     * @param outboxPartitionValues アウトボックスパーティション値
     * @return 生成されたメッセージID
     */
    private String insertIntoMessageTableDatabaseId(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            boolean published,
            CoreSchema coreSchema, OutboxPartitionValues outboxPartitionValues) {

        verifyNoID(headers);

        String serializedHeaders = JSonMapper.toJson(headers);

        long databaseId = coreJdbcStatementExecutor.insertAndReturnGeneratedId(coreJdbcOperationsUtils.insertIntoMessageTableDbIdSql(coreSchema, this::columnToJson, outboxPartitionValues.outboxTableSuffix.suffixAsString),
                MESSAGE_AUTO_GENERATED_ID_COLUMN, destination, serializedHeaders, payload, coreJdbcOperationsUtils.booleanToInt(published), outboxPartitionValues.messagePartition);

        return idGenerator.genId(databaseId, outboxPartitionValues.outboxTableSuffix.suffix).asString();
    }

    /**
     * SQL列をJSON形式に変換するためのメソッド。
     *
     * @param coreSchema 使用するスキーマ
     * @param column 変換対象のカラム名
     * @return JSON形式の列を含むSQL文字列
     */
    protected String columnToJson(CoreSchema coreSchema, String column) {
        return getCoreSqlDialect().castToJson(
                "?",
                coreSchema,
                "message",
                column,
                (sql, args) -> coreJdbcStatementExecutor.queryForList(sql, args.toArray())
        );
    }
}
