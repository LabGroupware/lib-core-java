/**
 * This package contains classes for consuming messages from the message broker.
 *
 * コンシューマの集大成で{@link MessageConsumer}とその実装クラスである{@link DefaultMessageConsumer}を提供する.
 *
 * これは,
 * {@link org.cresplanex.core.messaging.consumer.kafka.MessageConsumerKafkaImplementation}などの{@link MessageConsumerImplementation}の各実装クラスのラッパーであり,
 * 以下のことのみを行っている.
 * 
 * <ul>
 * <li>{@link org.cresplanex.core.messaging.consumer.subscribermap.SubscriberMapping}の利用</li>
 * <li>{@link org.cresplanex.core.messaging.consumer.decorator.DecoratedMessageHandlerFactory}の利用</li>
 * <li>{@link org.cresplanex.core.messaging.common.ChannelMapping}の利用</li>
 */
package org.cresplanex.core.messaging.consumer;
