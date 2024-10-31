package org.cresplanex.core.commands.consumer;

/**
 * 例外発生時のルートオブジェクト情報を格納するクラス。 パラメータと例外の原因情報を持ちます。
 */
public class DestinationForExceptionRootObject {

    /**
     * 処理対象のパラメータ
     */
    private final Object parameter;

    /**
     * 例外の原因
     */
    private final Throwable cause;

    /**
     * 処理対象のパラメータを取得します。
     *
     * @return 処理対象のパラメータ
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * 例外の原因を取得します。
     *
     * @return 例外の原因
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * 指定されたパラメータと例外原因で新しいインスタンスを作成します。
     *
     * @param parameter 処理対象のパラメータ
     * @param cause 例外の原因
     */
    public DestinationForExceptionRootObject(Object parameter, Throwable cause) {
        this.parameter = parameter;
        this.cause = cause;
    }
}
