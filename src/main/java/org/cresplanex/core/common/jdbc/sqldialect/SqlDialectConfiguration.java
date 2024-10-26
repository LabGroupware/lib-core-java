package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlDialectConfiguration {

  @Bean
  public MySqlDialect mySqlDialect() {
    return new MySqlDialect();
  }

  @Bean
  public PostgresDialect postgreSQLDialect() {
    return new PostgresDialect();
  }

  @Bean
  public MsSqlDialect msSqlDialect() {
    return new MsSqlDialect();
  }

  @Bean
  public DefaultCoreSqlDialect defaultSqlDialect(@Value("${core.current.time.in.milliseconds.sql:#{null}}") String customCurrentTimeInMillisecondsExpression) {
    return new DefaultCoreSqlDialect(customCurrentTimeInMillisecondsExpression);
  }

  @Bean
  public SqlDialectSelector sqlDialectSelector(Collection<CoreSqlDialect> dialects) {
    return new SqlDialectSelector(dialects);
  }
}
