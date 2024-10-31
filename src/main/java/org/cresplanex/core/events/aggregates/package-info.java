/**
 * This package contains the aggregate classes for the events.
 * 
 * {@link ResultWithDomainEvents}はドメインイベントを伴う操作の結果を表すクラスであり,
 * 操作の結果と発生したドメインイベントのリストを持つ.
 * 
 * {@link AbstractAggregateDomainEventPublisher}は集約に関連するドメインイベントを発行する抽象クラスであり,
 * 集約からIDを取得するための関数とイベントを発行するためのイベントパブリッシャーを持つ.
 */
package org.cresplanex.core.events.aggregates;
