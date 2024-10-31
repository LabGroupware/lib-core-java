package org.cresplanex.core.common.kafka.consumer;

/**
 * Kafkaのサブスクリプションクラス。
 * <p>
 * Kafkaサブスクリプションを閉じるためのコールバックを提供します。</p>
 */
public class KafkaSubscription {

    /**
     * サブスクリプションを閉じるためのコールバック関数。
     */
    private final Runnable closingCallback;

    /**
     * KafkaSubscriptionのコンストラクタ。
     *
     * @param closingCallback サブスクリプションを閉じるためのコールバック
     */
    public KafkaSubscription(Runnable closingCallback) {
        this.closingCallback = closingCallback;
    }

    /**
     * サブスクリプションを閉じます。
     * <p>
     * 設定されたコールバックを実行します。</p>
     */
    public void close() {
        closingCallback.run();
    }
}
