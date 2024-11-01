package org.cresplanex.core.saga.lock;

/**
 * メッセージを一時保管する必要があることを示す例外クラス。
 */
public class StashMessageRequiredException extends RuntimeException {

    private final String target;

    /**
     * 指定されたターゲットで StashMessageRequiredException を構築します。
     *
     * @param target 保管が必要なメッセージのターゲット
     */
    public StashMessageRequiredException(String target) {
        this.target = target;
    }

    /**
     * 保管対象のターゲットを取得します。
     *
     * @return 保管対象のターゲット
     */
    public String getTarget() {
        return target;
    }
}
