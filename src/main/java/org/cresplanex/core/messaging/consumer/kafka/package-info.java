/**
 * This package contains classes for consuming messages from Kafka.
 *
 * <p>
 * {@link org.cresplanex.core.common.kafka.consumer.CoreKafkaMessageConsumer}のラッパーであり,
 * {@link org.cresplanex.core.messaging.consumer.MessageConsumerImplementation}を実装するように調整.
 * ({@link org.cresplanex.core.common.kafka.consumer.KafkaMessageHandler}から{@link org.cresplanex.core.messaging.consumer.MessageHandler}への変換を行う.)
 * </p>
 */
package org.cresplanex.core.messaging.consumer.kafka;
