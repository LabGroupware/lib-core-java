package org.cresplanex.core.messaging.kafka.consumer;

import java.util.function.Consumer;

/**
 * Kafkaメッセージハンドラのインターフェース。
 * <p>
 * KafkaMessageを処理するための関数インターフェースです。</p>
 */
public interface KafkaMessageHandler extends Consumer<KafkaMessage> {

}
