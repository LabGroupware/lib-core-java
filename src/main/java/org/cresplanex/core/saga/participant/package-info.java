/**
 * This package contains the participants of the saga.
 *
 * {@link SagaReplyMessage} は、サガの応答メッセージを表すクラスです。 ロック対象を含む場合、ロック情報を格納し,
 * {@link SagaReplyMessageBuilder} は, この応答メッセージを構築するためのビルダークラスです。
 * また, {@link PostLockFunction} は、特定の条件に基づいてロック対象を決定するための関数を表します。
 * 
 * {@link SagaCommandDispatcher}では, サガのコマンドディスパッチャーを表し, このクラスは、サガのコマンドハンドラを管理します。
 * {@link SagaCommandDispatcherFactory} は、SagaCommandDispatcherのインスタンスを生成するファクトリクラスです。
 * また, {@link SagaCommandDispatcher}は{@link CommandDispatcher}を継承しており, メッセージのロックやアンロック、スタッシュ機能を使用してサガ内のコマンドメッセージを管理します。
 */
package org.cresplanex.core.saga.participant;
