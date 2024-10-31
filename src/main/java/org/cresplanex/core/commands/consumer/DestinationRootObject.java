package org.cresplanex.core.commands.consumer;

import java.util.Map;

/**
 * 宛先のルート情報を格納するクラス。 パラメータ、処理結果、およびパス変数の情報を持ちます。
 */
public class DestinationRootObject {

    /**
     * 処理対象のパラメータ
     */
    private final Object parameter;

    /**
     * 処理結果
     */
    private final Object result;

    /**
     * パス変数のマッピング
     */
    private final Map<String, String> path;

    /**
     * 指定されたパラメータ、結果、およびパス変数で新しいインスタンスを作成します。
     *
     * @param parameter 処理対象のパラメータ
     * @param result 処理結果
     * @param path パス変数のマッピング
     */
    public DestinationRootObject(Object parameter, Object result, Map<String, String> path) {
        this.parameter = parameter;
        this.result = result;
        this.path = path;
    }

    /**
     * 処理対象のパラメータを取得します。
     *
     * @return 処理対象のパラメータ
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * 処理結果を取得します。
     *
     * @return 処理結果
     */
    public Object getResult() {
        return result;
    }

    /**
     * パス変数のマッピングを取得します。
     *
     * @return パス変数のマッピング
     */
    public Map<String, String> getPath() {
        return path;
    }
}
