package org.cresplanex.core.common.kafka.consumer;

import java.util.function.Consumer;

import org.cresplanex.core.common.kafka.common.KafkaMessage;

/**
 * Kafkaメッセージハンドラのインターフェース。
 * <p>
 * KafkaMessageを処理するための関数インターフェースです。</p>
 */
public interface KafkaMessageHandler extends Consumer<KafkaMessage> {

}
