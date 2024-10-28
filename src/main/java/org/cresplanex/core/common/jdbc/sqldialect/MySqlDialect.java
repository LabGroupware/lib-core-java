package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.cresplanex.core.common.jdbc.JdbcUrlParser;

/**
 * MySQLおよびMariaDB用のSQL方言クラス。
 * <p>
 * MySQLおよびMariaDBのドライバとデータベース名に対応し、現在時刻の取得やIDの取得方法を独自に実装します。
 * </p>
 */
public class MySqlDialect extends AbstractCoreSqlDialect {

    /**
     * コンストラクタ。MySQLおよびMariaDB用の設定を行います。
     */
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
