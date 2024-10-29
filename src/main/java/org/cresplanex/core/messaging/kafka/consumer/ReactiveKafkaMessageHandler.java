package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Kafkaメッセージを非同期に処理するためのインターフェースです。
 * Kafkaメッセージを受け取り、完了を通知するCompletableFutureを返します。
 */
public interface ReactiveKafkaMessageHandler extends Function<KafkaMessage, CompletableFuture<Void>> {

}
