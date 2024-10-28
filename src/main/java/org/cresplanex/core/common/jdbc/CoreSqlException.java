package org.cresplanex.core.common.jdbc;

import java.sql.SQLException;

/**
 * SQL操作における例外を表すカスタム例外クラス。
 * <p>
 * この例外は、{@link SQLException} や他の {@link Throwable} オブジェクトを
 * ラップして、アプリケーション全体で統一された例外処理を提供します。
 * </p>
 */
public class CoreSqlException extends RuntimeException {

    /**
     * 指定されたメッセージを使用して CoreSqlException を構築します。
     *
     * @param message 例外メッセージ
     */
    public CoreSqlException(String message) {
        super(message);
    }

    /**
     * 指定された {@link SQLException} をラップして CoreSqlException を構築します。
     *
     * @param e ラップされる {@link SQLException} インスタンス
     */
    public CoreSqlException(SQLException e) {
        super(e);
    }

    /**
     * 任意の {@link Throwable} をラップして CoreSqlException を構築します。
     *
     * @param t ラップされる {@link Throwable} インスタンス
     */
    public CoreSqlException(Throwable t) {
        super(t);
    }
}
