package org.cresplanex.core.common.kafka.base;

/**
 * Kafkaメッセージプロセッサの処理に失敗した際の例外。
 * <p>
 * この例外は、Kafkaメッセージの処理中に発生したエラーを示します。
 * </p>
 */
public class KafkaMessageProcessorFailedException extends RuntimeException {

    /**
     * 指定された原因で例外を生成します。
     *
     * @param t 発生した原因となるThrowable
     */
    public KafkaMessageProcessorFailedException(Throwable t) {
        super(t);
    }
}
