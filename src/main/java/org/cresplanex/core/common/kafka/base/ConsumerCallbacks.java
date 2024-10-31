package org.cresplanex.core.common.kafka.base;

/**
 * Kafkaコンシューマのコミットに関連するコールバックを定義するインターフェース。
 * <p>
 * コミット処理の試行、成功、失敗に応じたコールバックメソッドを提供します。
 * </p>
 */
public interface ConsumerCallbacks {

    /**
     * コミット試行時に呼び出されるコールバック。
     */
    void onTryCommitCallback();

    /**
     * コミット成功時に呼び出されるコールバック。
     */
    void onCommitedCallback();

    /**
     * コミット失敗時に呼び出されるコールバック。
     */
    void onCommitFailedCallback();
}
