package org.cresplanex.core.common.jdbc;

import java.sql.SQLException;

public class CoreSqlException extends RuntimeException {

  public CoreSqlException(String message) {
    super(message);
  }

  public CoreSqlException(SQLException e) {
    super(e);
  }

  public CoreSqlException(Throwable t) {
    super(t);
  }
}
