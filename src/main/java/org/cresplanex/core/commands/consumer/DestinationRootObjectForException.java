package org.cresplanex.core.commands.consumer;

import java.util.Map;

/**
 * 例外が発生した際の宛先情報を格納するクラス。 `DestinationRootObject` クラスを継承し、例外の詳細を追加しています。
 */
public class DestinationRootObjectForException extends DestinationRootObject {

    /**
     * 発生した例外
     */
    private final Throwable throwable;

    /**
     * 発生した例外を取得します。
     *
     * @return 発生した例外
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * 指定されたパラメータ、結果、パス変数、および例外で新しいインスタンスを作成します。
     *
     * @param parameter 処理対象のパラメータ
     * @param result 処理結果
     * @param pathVars パス変数のマッピング
     * @param throwable 発生した例外
     */
    public DestinationRootObjectForException(Object parameter, Object result, Map<String, String> pathVars, Throwable throwable) {
        super(parameter, result, pathVars);
        this.throwable = throwable;
    }
}
