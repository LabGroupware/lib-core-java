/**
 * This package contains classes for multi-message processing in Kafka.
 * 
 * <p>
 * キーと値のペアを持つ{@link KeyValue}クラスを基底クラスとして,
 * マルチメッセージの一つ一つのメッセージとそのヘッダー{@link CoreKafkaMultiMessageHeader}を管理する{@link CoreKafkaMultiMessage}クラス,
 * マルチメッセージの集合とそのヘッダー{@link CoreKafkaMultiMessagesHeader}を管理する{@link CoreKafkaMultiMessages}クラスが含まれている.
 * 
 * これらのバイト変換を行うための{@link CoreKafkaMultiMessageConverter}クラスも含まれており, メッセージへの変換やメッセージのビルダも提供している.
 * </p>
 */
package org.cresplanex.core.common.kafka.multi;
