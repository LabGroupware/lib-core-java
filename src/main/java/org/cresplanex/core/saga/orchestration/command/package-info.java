/**
 * This package contains the command classes that are used to orchestrate the saga.
 * 
 * Saga orchestratorで使用されるコマンドクラスが含まれる.
 * {@link SagaCommandProducer}とその実装である{@link SagaCommandProducerImpl}は,
 * サーガ内のコマンドを生成および送信するためのインターフェースとその実装を提供する.
 * 
 * {@link org.cresplanex.core.commands.producer.CommandProducer}をラップしており,
 * 複数の{@link CommandWithDestinationAndType}を受け取って, コマンドを生成.
 * {@link CommandWithDestinationAndType}は, コマンドとその送信先, タイプ(コマンドか通知)を表す.
 * 
 * {@link SagaCommandProducerImpl}では, 全てのコマンドにおいて,
 * ヘッダーにサーガのタイプとIDを追加して, typeに応じて通知かコマンドを送信する.
 */
package org.cresplanex.core.saga.orchestration.command;
