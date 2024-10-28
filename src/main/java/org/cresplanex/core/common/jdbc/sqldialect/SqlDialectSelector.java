package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * SQL方言の選択を行うクラス。
 * <p>
 * ドライバ名またはデータベース名に基づいて適切なSQL方言（CoreSqlDialect）を選択します。
 * データベースのドライバや名前に一致する方言が複数ある場合、優先度の高い順に選択されます。
 * </p>
 */
public class SqlDialectSelector {

    /**
     * 使用可能なSQL方言のコレクション
     */
    private final Collection<CoreSqlDialect> sqlDialects;

    /**
     * コンストラクタ。利用可能なSQL方言を設定します。
     *
     * @param sqlDialects 使用可能なSQL方言のコレクション
     */
    public SqlDialectSelector(Collection<CoreSqlDialect> sqlDialects) {
        this.sqlDialects = sqlDialects;
    }

    /**
     * 指定されたドライバに対応するSQL方言を取得します。
     *
     * @param driver 対応するSQLドライバ名
     * @return ドライバに対応するCoreSqlDialectのインスタンス
     */
    public CoreSqlDialect getDialect(String driver) {
        return getDialect(dialect -> dialect.supports(driver), driver);
    }

    /**
     * 指定されたデータベース名およびオプションのドライバ名に対応するSQL方言を取得します。
     *
     * @param name データベース名
     * @param driver SQLドライバ名のオプション
     * @return データベース名およびドライバに対応するCoreSqlDialectのインスタンス
     */
    public CoreSqlDialect getDialect(String name, Optional<String> driver) {

        // Error message for unsupported dialect
        String failMessage = name.concat(driver.map("/"::concat).orElse(""));

        return getDialect(dialect -> driver.map(dialect::supports).orElse(false) || dialect.accepts(name), failMessage);
    }

    /**
     * 指定された条件に一致するSQL方言を取得します。
     * <p>
     * 条件に一致するSQL方言が複数ある場合、優先度の高い順に選択されます。
     * </p>
     *
     * @param predicate 条件を指定する述語
     * @param searchFailedMessage 一致するSQL方言が見つからない場合のメッセージ
     * @return 条件に一致するCoreSqlDialectのインスタンス
     * @throws IllegalStateException 一致するSQL方言が見つからなかった場合
     */
    private CoreSqlDialect getDialect(Predicate<CoreSqlDialect> predicate, String searchFailedMessage) {
        return sqlDialects
                .stream()
                .filter(predicate)
                .min(Comparator.comparingInt(CoreSqlDialectOrder::getOrder))
                .orElseThrow(() -> new IllegalStateException(
                String.format("Sql Dialect not found (%s), "
                        + "you can specify environment variable '%s' to solve the issue",
                        searchFailedMessage,
                        "CORE_CURRENT_TIME_IN_MILLISECONDS_SQL")));
    }
}
