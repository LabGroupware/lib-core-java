package org.cresplanex.core.common.jdbc;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SchemaAndTable {

    private final String schema;
    private final String tableName;

    public SchemaAndTable(String schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName.toLowerCase();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, tableName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SchemaAndTable that = (SchemaAndTable) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }

    // @Override
    // public boolean equals(Object obj) {
    //     return EqualsBuilder.reflectionEquals(this, obj);
    // }
}
