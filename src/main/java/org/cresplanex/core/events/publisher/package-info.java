/**
 * eventの発行を行うクラスを提供する.
 * 
 * {@link DomainEventPublisher}はeventを発行するためのインターフェースであり, この実装クラスである{@link DomainEventPublisherImpl}は
 * eventの発行を行うための実装を提供する.
 * 
 * {@link org.cresplanex.core.messaging.producer.MessageProducer}をラップしており, eventをメッセージとして送信する.
 * ヘッダー情報を付与するものと付与しないものの2つのメソッドを提供している.
 * Listで渡されたeventを1つずつMessageProducerに渡している.
 * 内部では{@link org.cresplanex.core.events.common.EventUtil}を使用してヘッダーの設定などを行っている.
 */
package org.cresplanex.core.events.publisher;
