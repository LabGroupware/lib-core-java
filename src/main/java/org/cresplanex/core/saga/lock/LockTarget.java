package org.cresplanex.core.saga.lock;

/**
 * ロック対象を表すクラスです。 {@code Class}やIDを基にロック対象を特定します。
 */
public class LockTarget {

    private String target;

    /**
     * 指定されたクラスとIDを用いて{@code LockTarget}のインスタンスを作成します。
     *
     * @param targetClass ロック対象のクラス
     * @param targetId ロック対象のID
     */
    public LockTarget(Class<?> targetClass, Object targetId) {
        this(targetClass.getName(), targetId.toString());
    }

    /**
     * 指定されたクラス名とID文字列を用いて{@code LockTarget}のインスタンスを作成します。
     *
     * @param targetClass ロック対象のクラス名
     * @param targetId ロック対象のID文字列
     */
    public LockTarget(String targetClass, String targetId) {
        this(targetClass + "/" + targetId);
    }

    /**
     * 指定されたターゲット文字列を用いて{@code LockTarget}のインスタンスを作成します。
     *
     * @param target ターゲット文字列
     */
    public LockTarget(String target) {
        this.target = target;
    }

    /**
     * ロック対象を取得します。
     *
     * @return ロック対象の文字列
     */
    public String getTarget() {
        return target;
    }
}
