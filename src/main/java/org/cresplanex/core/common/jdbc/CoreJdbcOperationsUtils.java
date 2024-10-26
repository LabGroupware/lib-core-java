package org.cresplanex.core.common.jdbc;

import org.cresplanex.core.common.jdbc.sqldialect.CoreSqlDialect;

public class CoreJdbcOperationsUtils {

  public static final String MESSAGE_AUTO_GENERATED_ID_COLUMN = "dbid";
  public static final String EVENT_AUTO_GENERATED_ID_COLUMN = "id";

  public static final String MESSAGE_APPLICATION_GENERATED_ID_COLUMN = "id";
  public static final String EVENT_APPLICATION_GENERATED_ID_COLUMN = "event_id";

  private final CoreSqlDialect coreSqlDialect;

  public CoreJdbcOperationsUtils(CoreSqlDialect coreSqlDialect) {
    this.coreSqlDialect = coreSqlDialect;
  }


  public String insertIntoEventsTableApplicationIdSql(CoreSchema coreSchema) {
    return String.format("INSERT INTO %s (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)", coreSchema.qualifyTable("events"));
  }

  public String insertIntoEventsTableDbIdSql(CoreSchema coreSchema) {
    return String.format("INSERT INTO %s (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)" +
            " VALUES ('', ?, ?, ?, ?, ?, ?, ?)", coreSchema.qualifyTable("events"));
  }

  public String insertIntoMessageTableApplicationIdSql(CoreSchema coreSchema, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
    return insertIntoMessageTable(coreSchema,
            "insert into %s%s(id, destination, headers, payload, creation_time, published, message_partition) values(?, ?, %s, %s, %s, ?, ?)",
            jsonConverter, outboxTableSuffix);
  }

  public String insertIntoMessageTableDbIdSql(CoreSchema coreSchema, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
    return insertIntoMessageTable(coreSchema,
            "insert into %s%s(id, destination, headers, payload, creation_time, published, message_partition) values('', ?, %s, %s, %s, ?, ?)",
            jsonConverter, outboxTableSuffix);
  }

  public String insertIntoMessageTable(CoreSchema coreSchema, String sql, SqlJsonConverter jsonConverter, String outboxTableSuffix) {
    return String.format(sql,
            coreSchema.qualifyTable("message"),
            outboxTableSuffix,
            jsonConverter.convert(coreSchema, "headers"),
            jsonConverter.convert(coreSchema, "payload"),
            coreSqlDialect.getCurrentTimeInMillisecondsExpression());
  }

  public int booleanToInt(boolean bool) {
    return bool ? 1 : 0;
  }
}
