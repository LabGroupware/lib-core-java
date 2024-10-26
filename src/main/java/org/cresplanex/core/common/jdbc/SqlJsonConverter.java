package org.cresplanex.core.common.jdbc;

public interface SqlJsonConverter {
  String convert(CoreSchema schema, String column);
}
