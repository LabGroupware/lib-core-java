package org.cresplanex.core.commands.common;

/**
 * 処理が失敗した場合の結果を表すクラス。 {@link Outcome} インターフェースを実装し、失敗結果の表現を提供します。
 */
public class Failure implements Outcome {

    public static final String TYPE = "core.failure";
}
