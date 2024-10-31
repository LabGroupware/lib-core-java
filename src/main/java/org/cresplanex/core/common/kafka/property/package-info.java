/**
 * This package contains the classes that are used to configure the Kafka properties.
 * 
 * {@link CoreKafkaConsumerProperties}でKafkaコンシューマーの設定を行うためのクラスを提供し,
 * デフォルトでは, Springプロパティによる注入を行う{@link CoreKafkaConsumerSpringProperties}が用意されている.
 * 
 * また, {@link ConsumerPropertiesFactory}では, 最低限のプロパティ生成を行っている.
 * ただし, 利用する側の{@link  org.cresplanex.core.common.kafka.base.CoreKafkaConsumer}では,
 * {@link CoreKafkaConsumerProperties}で上書きを行うため, カスタマイズも可能である.
 * 
 * {@link CoreKafkaConnectProperties}では, Kafkaの接続設定を保持するクラスを提供している.
 * {@link org.cresplanex.core.common.kafka.consumer.CoreKafkaMessageConsumer}の実装により, {@link ConsumerPropertiesFactory}での
 * bootstrapServers(アドレス)はこのクラスから取得される.
 * 
 */
package org.cresplanex.core.common.kafka.property;
