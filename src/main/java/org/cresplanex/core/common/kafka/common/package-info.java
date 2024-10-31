/**
 * Common classes used by clients and servers.
 *
 * <p>
 * kafkaパッケージ内で使用する共通クラスを提供する. {@link CoreBinaryMessageEncoding}では,
 * バイナリ->String, String->バイナリの変換を提供.
 *
 * {@link KafkaMessage}は, Kafkaのメッセージを表すクラス. {@link RawKafkaMessage}は,
 * Swimlaneなどで使用される生のKafkaメッセージを表すクラス.
 * </p>
 */
package org.cresplanex.core.common.kafka.common;
