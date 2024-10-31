/**
 * Lower level Kafka API.
 * 
 * <p>
 * {@link KafkaMessageConsumer}インタフェースによって提供される, Kafkaメッセージの消費に関連するAPIを用いた操作と,
 * そのファクトリ{@link KafkaMessageConsumerFactory}を提供する.
 * デフォルトでは, {@link DefaultKafkaMessageConsumer}と{@link DefaultKafkaMessageConsumerFactory}が提供されており, KafkaのSDKをラップしている.
 * </p>
 */
package org.cresplanex.core.common.kafka.lower;
