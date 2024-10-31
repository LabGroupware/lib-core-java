/**
 * This package contains the subscriber classes for the events.
 *
 * このパッケージでの成果物は, {@link DomainEventDispatcherFactory} と
 * {@link DomainEventDispatcher}, {@link DomainEventHandlersBuilder}, {@link DomainEventHandlers}
 * である. である.
 *
 * まず, {@link DomainEventHandlersBuilder}で {@link DomainEventHandler} を生成し,
 * {@link DomainEventDispatcherFactory} で {@link DomainEventDispatcher}
 * を生成するという流れとなる.
 *
 * {@link DomainEventHandlers} は, {@link DomainEventHandler} のリストを保持するクラスである.
 * {@link DomainEventHandler} では, ハンドル可能なメッセージかどうかを判定する
 * {@link #handles(Message)} メソッド(具体的にはイベントの型と集約の型が一致するかどうかを判定する)と, ハンドラを実行する
 * {@link #invoke(DomainEventEnvelope)} メソッドが定義されている.
 *
 * {@link DomainEventHandlers}では, 自身が管理する集約タイプのセットを取得したり,
 * 指定されたメッセージを処理可能なハンドラを見つける {@link #findTargetMethod(Message)} メソッドが定義されている.
 *
 * {@link DomainEventHandlersBuilder} では, aggregateTypeとonEvent(Class<E>
 * eventClass, Consumer<DomainEventEnvelope<E>> handler)によるハンドラの登録を行う.
 * Consumerのみの登録であるため, DomainEventEnvelope<E>を利用した一方的な処理を行うのみで返答はしないことを想定している.
 * 
 * {@link DomainEventDispatcherFactory} では, {@link DomainEventDispatcher} を生成後, initializeメソッドを呼び出している.
 * ここでは, 自身のmessageHandlerを使用してsubscribeを行っている.
 * 
 * AGGREGATE_TYPEの取得, EVENT_TYPEの変換, ハンドラの取得, メッセージのパース, ハンドラの呼び出しを行っている.
 *
 */
package org.cresplanex.core.events.subscriber;
