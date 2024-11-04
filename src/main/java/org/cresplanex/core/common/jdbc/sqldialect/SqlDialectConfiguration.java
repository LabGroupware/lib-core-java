package org.cresplanex.core.common.jdbc.sqldialect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SQL方言の設定クラス。
 * <p>
 * 各SQL方言のBeanを作成し、SQL方言セレクターを作成します。
 * </p>
 */
@Configuration
public class SqlDialectConfiguration {

    /**
     * MySQL用のSQL方言Beanを作成します。
     *
     * @return MySQL用のSQL方言Bean
     */
    @Bean("org.cresplanex.core.common.jdbc.sqldialect.MySqlDialect")
    public MySqlDialect mySqlDialect() {
        return new MySqlDialect();
    }

    /**
     * PostgreSQL用のSQL方言Beanを作成します。
     *
     * @return PostgreSQL用のSQL方言Bean
     */
    @Bean("org.cresplanex.core.common.jdbc.sqldialect.PostgresDialect")
    public PostgresDialect postgreSQLDialect() {
        return new PostgresDialect();
    }

    /**
     * SQL Server用のSQL方言Beanを作成します。
     *
     * @return SQL Server用のSQL方言Bean
     */
    @Bean("org.cresplanex.core.common.jdbc.sqldialect.MsSqlDialect")
    public MsSqlDialect msSqlDialect() {
        return new MsSqlDialect();
    }

    /**
     * default用のSQL方言Beanを作成します。
     *
     * @param customCurrentTimeInMillisecondsExpression
     * @return
     */
    @Bean("org.cresplanex.core.common.jdbc.sqldialect.DefaultCoreSqlDialect")
    public DefaultCoreSqlDialect defaultSqlDialect(@Value("${core.current.time.in.milliseconds.sql:#{null}}") String customCurrentTimeInMillisecondsExpression) {
        return new DefaultCoreSqlDialect(customCurrentTimeInMillisecondsExpression);
    }
}
