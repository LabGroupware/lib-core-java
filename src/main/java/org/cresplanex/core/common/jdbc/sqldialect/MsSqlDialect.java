package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collections;

/**
 * Microsoft SQL Server用のSQL方言クラス。
 * <p>
 * MS SQL Serverのドライバとデータベース名に対応し、LIMIT句の追加方法を独自に実装します。
 * </p>
 */
public class MsSqlDialect extends AbstractCoreSqlDialect {

    /**
     * コンストラクタ。MS SQL Server用の現在時刻取得式を設定します。
     */
    public MsSqlDialect() {
        super(Collections.singleton("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
                Collections.singleton("mssql"), "(SELECT DATEDIFF_BIG(ms, '1970-01-01', GETUTCDATE()))");
    }

    @Override
    public String addLimitToSql(String sql, String limitExpression) {
        String newSql = sql.replaceFirst("(?i:select)", String.format("select top (%s)", limitExpression));
        if (newSql.equals(sql)) {
            throw new IllegalArgumentException("Cannot add limit to SQL: " + sql);
        }
        return newSql;
    }
}
