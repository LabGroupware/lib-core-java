package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.cresplanex.core.common.jdbc.JdbcUrlParser;

public class MySqlDialect extends AbstractCoreSqlDialect {

  public MySqlDialect() {
    super(new HashSet<>(Arrays.asList("com.mysql.cj.jdbc.Driver", "com.mysql.jdbc.Driver")),
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList("mysql", "mariadb"))),
            "ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000)");
  }

  @Override
  public String getJdbcCatalogue(String dataSourceUrl) {
    return JdbcUrlParser.parse(dataSourceUrl).getDatabase();
  }

  @Override
  public String addReturningOfGeneratedIdToSql(String sql, String idColumn) {
    return sql.concat("; SELECT LAST_INSERT_ID();");
  }
}
