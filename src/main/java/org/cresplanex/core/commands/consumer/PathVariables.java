package org.cresplanex.core.commands.consumer;

import java.util.Map;

/**
 * パス変数の値を格納および取得するためのクラス。 パスに含まれる変数をキーとして、値をマップとして管理します。
 */
public class PathVariables {

    /**
     * パス変数の名前とその値のマッピング
     */
    private final Map<String, String> pathVars;

    /**
     * 指定されたパス変数マップで {@code PathVariables} のインスタンスを生成します。
     *
     * @param pathVars パス変数のマッピング
     */
    public PathVariables(Map<String, String> pathVars) {
        this.pathVars = pathVars;
    }

    /**
     * 指定された名前のパス変数の値を文字列として取得します。
     *
     * @param name 取得するパス変数の名前
     * @return パス変数の値
     */
    public String getString(String name) {
        return pathVars.get(name);
    }

    /**
     * 指定された名前のパス変数の値を {@code long} 型として取得します。
     *
     * @param name 取得するパス変数の名前
     * @return パス変数の値を {@code long} 型で返す
     */
    public long getLong(String name) {
        return Long.parseLong(getString(name));
    }
}
