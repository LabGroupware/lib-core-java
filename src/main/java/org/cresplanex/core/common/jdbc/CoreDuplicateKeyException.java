package org.cresplanex.core.common.jdbc;

import java.sql.SQLException;

public class CoreDuplicateKeyException extends CoreSqlException {
  public CoreDuplicateKeyException(SQLException sqlException) {
    super(sqlException);
  }

  public CoreDuplicateKeyException(Throwable throwable) {
    super(throwable);
  }
}
