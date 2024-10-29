package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Kafkaコンシューマーメッセージハンドラを表すインターフェース。
 * <p>
 * 各メッセージを処理し、必要に応じてバックログの情報を返します。
 * </p>
 */
public interface CoreKafkaConsumerMessageHandler
        extends BiFunction<ConsumerRecord<String, byte[]>, BiConsumer<Void, Throwable>, MessageConsumerBacklog> {
}
