package org.cresplanex.core.common.jdbc.sqldialect;

/**
 * SQL方言の順序を表すインターフェース。
 * <p>
 * SQL方言の順序を表すインターフェースです。
 * </p>
 */
public interface CoreSqlDialectOrder {

    /**
     * SQL方言の順序を取得します。
     *
     * @return SQL方言の順序
     */
    int getOrder();
}
