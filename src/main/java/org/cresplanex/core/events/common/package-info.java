/**
 * This package contains common event classes.
 * 
 * {@link DomainEvent}を実装するクラスのみeventとして扱うことができ,
 * イベントネームは{@link DomainEventNameMapping}によって外部イベントタイプに変換される.
 * この実装クラスとして{@link DefaultDomainEventNameMapping}が用意されている.
 * 
 * また, {@link EventUtil}はドメインイベント用のユーティリティクラスであり,
 * ドメインイベントのメッセージを作成するためのメソッドが提供されている.
 * 
 * {@link DomainEventEnvelope}はドメインイベントをラップするエンベロープインターフェースであり,
 * {@link DomainEventEnvelopeImpl}はその具体的な実装クラスである.
 */
package org.cresplanex.core.events.common;
