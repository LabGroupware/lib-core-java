/**
 * This package contains classes that are responsible for sending messages to the message broker.
 *
 * メッセージブローカーにメッセージを送信するためのクラスが含まれるパッケージ.
 * 成果物としては, {@link MessageProducer}とその実装である{@link MessageProducerImpl}が含まれる.
 *
 * {@link org.cresplanex.core.messaging.producer.jdbc.MessageProducerJdbcImplementation}などの{@link MessageProducerImplementation}を実装するクラスのラッパーであり,
 * 以下の点を行っている.
 * <ul>
 * <li>{@link org.cresplanex.core.messaging.common.MessageInterceptor}のpreSend,
 * postSendの処理挿入</li>
 * <li>前処理として, 1. 実装クラスにsetMessageIdIfNecessaryがあれば, その呼び出しによりメッセージIDを設定, 2.
 * {@link org.cresplanex.core.messaging.common.ChannelMapping}を用いたDESTINATIONヘッダーの設定,
 * 3. {@link HttpDateHeaderFormatUtil}を用いたDATEヘッダーへの現在時刻設定, 4.
 * PARTITION_IDヘッダーがnullの場合にuuidを設定.</li>
 * </ul>
 */
package org.cresplanex.core.messaging.producer;
