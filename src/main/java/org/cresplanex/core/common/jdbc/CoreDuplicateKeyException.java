package org.cresplanex.core.common.jdbc;

import java.sql.SQLException;

/**
 * データベースの重複キーエラーを表すカスタム例外クラス。
 * <p>
 * {@link SQLException} や他の {@link Throwable} をラップして、重複キーエラーが発生したことを示します。
 * </p>
 */
public class CoreDuplicateKeyException extends CoreSqlException {

    /**
     * 指定された {@link SQLException} をラップして CoreDuplicateKeyException を構築します。
     *
     * @param sqlException 重複キーエラーを含む {@link SQLException} インスタンス
     */
    public CoreDuplicateKeyException(SQLException sqlException) {
        super(sqlException);
    }

    /**
     * 任意の {@link Throwable} をラップして CoreDuplicateKeyException を構築します。
     *
     * @param throwable 重複キーエラーを含む {@link Throwable} インスタンス
     */
    public CoreDuplicateKeyException(Throwable throwable) {
        super(throwable);
    }
}
