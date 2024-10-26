package org.cresplanex.core.common.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.common.id.IdGenerator;
import static org.cresplanex.core.common.jdbc.CoreJdbcOperationsUtils.EVENT_AUTO_GENERATED_ID_COLUMN;
import static org.cresplanex.core.common.jdbc.CoreJdbcOperationsUtils.MESSAGE_AUTO_GENERATED_ID_COLUMN;
import org.cresplanex.core.common.jdbc.sqldialect.CoreSqlDialect;
import org.cresplanex.core.common.json.mapper.JSonMapper;

public class CoreCommonJdbcOperations {

    private final CoreJdbcOperationsUtils coreJdbcOperationsUtils;
    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final OutboxPartitioningSpec outboxPartitioningSpec;
    private final CoreSqlDialect coreSqlDialect;

    public CoreCommonJdbcOperations(CoreJdbcOperationsUtils coreJdbcOperationsUtils,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSqlDialect coreSqlDialect,
            OutboxPartitioningSpec outboxPartitioningSpec) {
        this.coreJdbcOperationsUtils = coreJdbcOperationsUtils;
        this.coreSqlDialect = coreSqlDialect;
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        this.outboxPartitioningSpec = outboxPartitioningSpec;
    }

    public CoreSqlDialect getCoreSqlDialect() {
        return coreSqlDialect;
    }

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

    public String insertIntoMessageTable(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            CoreSchema coreSchema) {

        return insertIntoMessageTable(idGenerator, payload, destination, headers, coreSchema, false);
    }

    public String insertPublishedMessageIntoMessageTable(IdGenerator idGenerator,
            String payload,
            String destination,
            Map<String, String> headers,
            CoreSchema coreSchema) {

        return insertIntoMessageTable(idGenerator, payload, destination, headers, coreSchema, true);
    }

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

    private void verifyNoID(Map<String, String> headers) {
        if (headers.containsKey("ID")) {
            throw new RuntimeException("ID should not be already set");
        }
    }

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
