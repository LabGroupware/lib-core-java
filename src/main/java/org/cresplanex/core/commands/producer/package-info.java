/**
 * This package contains the classes that are used to implement the commands for the producer.
 * 
 * このパッケージでの成果物は, {@link CommandProducer} とその実装クラスである {@link CommandProducerImpl}.
 * 
 * {@link CommadMessageFactory} は、{@link CommandProducerImpl} で使用されるユーティリティクラスであり, 
 * Messageの組み立てを行うためのメソッドを提供している.
 * 具体的には, payloadのtoJson, DESTINATION, COMMAND_TYPE, REPLY_TO, RESOURCE のヘッダーを設定している.
 * 
 * {@link CommandProducerImpl} は, {@link org.cresplanex.core.messaging.producer.MessageProducer}のほとんどラッパーであり,
 * 3種類のsendメソッドを提供している.
 * 1つは, リソースを指定してコマンドを送信するメソッドであり, 他の2つは, 通常のコマンドを送信するメソッドである.
 * 3つ目は, 通知としてコマンドを送信するメソッド(replyの期待をしない)である.
 */
package org.cresplanex.core.commands.producer;
