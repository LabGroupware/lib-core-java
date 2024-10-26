package org.cresplanex.core.common.jdbc;

import org.apache.commons.lang3.StringUtils;

public class CoreSchema {
  public static final String DEFAULT_SCHEMA = "core";
  public static final String EMPTY_SCHEMA = "none";

  private final String coreDatabaseSchema;

  public CoreSchema() {
    coreDatabaseSchema = DEFAULT_SCHEMA;
  }

  public CoreSchema(String coreDatabaseSchema) {
    this.coreDatabaseSchema = StringUtils.isEmpty(coreDatabaseSchema) ? DEFAULT_SCHEMA : coreDatabaseSchema;
  }

  public String getCoreDatabaseSchema() {
    return coreDatabaseSchema;
  }

  public boolean isEmpty() {
    return EMPTY_SCHEMA.equals(coreDatabaseSchema);
  }

  public boolean isDefault() {
    return DEFAULT_SCHEMA.equals(coreDatabaseSchema);
  }

  public String qualifyTable(String table) {
    if (isEmpty()) return table;

    String schema = isDefault() ? DEFAULT_SCHEMA : coreDatabaseSchema;

    return String.format("%s.%s", schema, table);
  }
}
