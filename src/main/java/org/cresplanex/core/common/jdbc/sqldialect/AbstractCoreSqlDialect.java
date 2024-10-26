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

public abstract class AbstractCoreSqlDialect implements CoreSqlDialect {

  private final Set<String> drivers;
  private final Set<String> names;
  private final String customCurrentTimeInMillisecondsExpression;

  public AbstractCoreSqlDialect(Set<String> drivers, Set<String> names, String customCurrentTimeInMillisecondsExpression) {
    this.drivers = drivers;
    this.names = names;
    this.customCurrentTimeInMillisecondsExpression = customCurrentTimeInMillisecondsExpression;
  }

  @Override
  public boolean supports(String driver) {
    return drivers.contains(driver);
  }

  @Override
  public boolean accepts(String name) {
    return names.contains(name);
  }

  @Override
  public String addLimitToSql(String sql, String limitExpression) {
    return String.format("%s limit %s", sql, limitExpression);
  }

  @Override
  public String addReturningOfGeneratedIdToSql(String sql, String idColumn) {
    throw new NotImplementedException();
  }

  @Override
  public String getCurrentTimeInMillisecondsExpression() {
    return customCurrentTimeInMillisecondsExpression;
  }

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

  protected String getJdbcCatalogue(String dataSourceUrl) {
    return null;
  }

  @Override
  public int getOrder() {
    return Integer.MIN_VALUE;
  }
}
