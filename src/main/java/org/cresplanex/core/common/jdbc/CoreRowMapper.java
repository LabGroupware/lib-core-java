package org.cresplanex.core.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CoreRowMapper<T> {
  T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
