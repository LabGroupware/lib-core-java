package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class SqlDialectSelector {
  private final Collection<CoreSqlDialect> sqlDialects;

  public SqlDialectSelector(Collection<CoreSqlDialect> sqlDialects) {
    this.sqlDialects = sqlDialects;
  }

  public CoreSqlDialect getDialect(String driver) {
    return getDialect(dialect -> dialect.supports(driver), driver);
  }

  public CoreSqlDialect getDialect(String name, Optional<String> driver) {

    String failMessage = name.concat(driver.map("/"::concat).orElse(""));

    return getDialect(dialect -> driver.map(dialect::supports).orElse(false) || dialect.accepts(name), failMessage);

  }

  private CoreSqlDialect getDialect(Predicate<CoreSqlDialect> predicate, String searchFailedMessage) {
    return sqlDialects
            .stream()
            .filter(predicate)
            .min(Comparator.comparingInt(CoreSqlDialectOrder::getOrder))
            .orElseThrow(() ->
                    new IllegalStateException(String.format("Sql Dialect not found (%s), " +
                                    "you can specify environment variable '%s' to solve the issue",
                            searchFailedMessage,
                            "CORE_CURRENT_TIME_IN_MILLISECONDS_SQL")));
  }
}
