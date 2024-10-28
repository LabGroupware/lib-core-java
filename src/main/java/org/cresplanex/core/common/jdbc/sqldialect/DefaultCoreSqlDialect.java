package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collections;

/**
 * デフォルトのSQL方言クラス。
 * <p>
 * すべてのドライバとデータベース名をサポートし、カスタムの現在時刻取得式を使用します。
 * </p>
 */
public class DefaultCoreSqlDialect extends AbstractCoreSqlDialect {

    /**
     * コンストラクタ。カスタムの現在時刻取得式を設定します。
     *
     * @param customCurrentTimeInMillisecondsExpression 現在の時間（ミリ秒）を取得するカスタム式
     */
    public DefaultCoreSqlDialect(String customCurrentTimeInMillisecondsExpression) {
        super(Collections.emptySet(), Collections.emptySet(), customCurrentTimeInMillisecondsExpression);
    }

    @Override
    public boolean supports(String driver) {
        return true;
    }

    @Override
    public boolean accepts(String name) {
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
