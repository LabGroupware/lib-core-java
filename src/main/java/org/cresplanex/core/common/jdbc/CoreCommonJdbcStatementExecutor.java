package org.cresplanex.core.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class CoreCommonJdbcStatementExecutor implements CoreJdbcStatementExecutor {

  private static final Set<Integer> DUPLICATE_KEY_ERROR_CODES = new HashSet<>(Arrays.asList(
          1062, // MySQL
          2601, 2627, // MS-SQL
          23505, // Postgres
          23001 // H2
  ));

  private final Supplier<Connection> connectionProvider;

  public CoreCommonJdbcStatementExecutor(Supplier<Connection> connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  @Override
  public long insertAndReturnGeneratedId(String sql, String idColumn, Object... parameters) {
    Connection connection = connectionProvider.get();

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      for (int i = 1; i <= parameters.length; i++) {
        preparedStatement.setObject(i, parameters[i - 1]);
      }

      preparedStatement.executeUpdate();

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {

          if (generatedKeys.getMetaData().getColumnCount() == 1) {
            // generate id can have different name than column (GENERATED_ID for mysql)
            return generatedKeys.getLong(1);
          }

          //contains all columns for postgres
          return generatedKeys.getLong(idColumn);
        }
        else {
          throw new CoreSqlException("Id was not generated");
        }
      }
    } catch (SQLException e) {
      handleSqlUpdateException(e);

      return -1; //should not be here
    }
  }

  @Override
  public int update(String sql, Object... parameters) {
    Connection connection = connectionProvider.get();

    try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      for (int i = 1; i <= parameters.length; i++) {
        preparedStatement.setObject(i, parameters[i - 1]);
      }

      return preparedStatement.executeUpdate();
    }
    catch (SQLException e) {
      handleSqlUpdateException(e);

      return 0; //should not be here
    }
  }

  @Override
  public <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... parameters) {
    Connection connection = connectionProvider.get();

    try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      for (int i = 1; i <= parameters.length; i++) {
        preparedStatement.setObject(i, parameters[i - 1]);
      }

      ResultSet rs = preparedStatement.executeQuery();

      List<T> result = new ArrayList<>();

      int rowNum = 0;
      while (rs.next()) {
        result.add(coreRowMapper.mapRow(rs, rowNum++));
      }

      return result;
    }
    catch (SQLException e) {
      throw new CoreSqlException(e);
    }
  }

  @Override
  public List<Map<String, Object>> queryForList(String sql, Object... parameters) {
    Connection connection = connectionProvider.get();

    try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      for (int i = 1; i <= parameters.length; i++) {
        preparedStatement.setObject(i, parameters[i - 1]);
      }

      ResultSet rs = preparedStatement.executeQuery();

      List<Map<String, Object>> result = new ArrayList<>();

      while (rs.next()) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
        }
        result.add(row);
      }

      return result;
    }
    catch (SQLException e) {
      throw new CoreSqlException(e);
    }
  }

  private void handleSqlUpdateException(SQLException e) {
    Optional<Integer> additionalErrorCode = Optional.empty();

    // Workaround for postgres, where e.getErrorCode() is 0
    try {
      // additionalErrorCode = Optional.of(Integer.parseInt(e.getSQLState()));
      additionalErrorCode = Optional.of(Integer.valueOf(e.getSQLState()));
    } catch (NumberFormatException nfe) {
      // ignore
    }

    if (DUPLICATE_KEY_ERROR_CODES.contains(e.getErrorCode()) ||
            additionalErrorCode.map(DUPLICATE_KEY_ERROR_CODES::contains).orElse(false)) {

      throw new CoreDuplicateKeyException(e);
    }

    throw new CoreSqlException(e);
  }
}
