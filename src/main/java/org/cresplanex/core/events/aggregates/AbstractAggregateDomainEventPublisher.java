package org.cresplanex.core.events.aggregates;

import java.util.List;
import java.util.function.Function;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.publisher.DomainEventPublisher;

/**
 * 集約に関連するドメインイベントを発行する抽象クラスです。
 *
 * @param <A> 集約タイプ
 * @param <E> ドメインイベントタイプ
 */
public abstract class AbstractAggregateDomainEventPublisher<A, E extends DomainEvent> {

    /**
     * 集約からIDを取得するための関数
     */
    private final Function<A, Object> idSupplier;

    /**
     * イベントを発行するためのイベントパブリッシャー
     */
    private final DomainEventPublisher eventPublisher;

    /**
     * 集約のクラス型
     */
    private final Class<A> aggregateClass;

    /**
     * 集約の型名
     */
    private final String aggregateTypeName;

    /**
     * コンストラクタ。集約イベントパブリッシャーの基本的な情報を設定します。
     *
     * @param eventPublisher イベントを発行するためのインスタンス
     * @param aggregateClass 集約のクラス型
     * @param idSupplier 集約からIDを取得するための関数
     * @param aggregateTypeName 集約の型名
     */
    protected AbstractAggregateDomainEventPublisher(DomainEventPublisher eventPublisher,
            Class<A> aggregateClass,
            Function<A, Object> idSupplier,
            String aggregateTypeName) {
        this.eventPublisher = eventPublisher;
        this.aggregateClass = aggregateClass;
        this.idSupplier = idSupplier;
        this.aggregateTypeName = aggregateTypeName;
    }

    protected AbstractAggregateDomainEventPublisher(DomainEventPublisher eventPublisher,
            Class<A> aggregateClass,
            Function<A, Object> idSupplier) {
        this(eventPublisher, aggregateClass, idSupplier, aggregateClass.getSimpleName());
    }

    /**
     * 集約のクラス型を返します。
     *
     * @return 集約のクラス型
     */
    public Class<A> getAggregateClass() {
        return aggregateClass;
    }

    /**
     * 指定した集約およびイベントリストを使用して、ドメインイベントを発行します。
     *
     * @param aggregate イベントが関連する集約
     * @param events 発行するイベントリスト
     */
    @SuppressWarnings("unchecked")
    public void publish(A aggregate, List<E> events) {
        eventPublisher.publish(aggregateTypeName, idSupplier.apply(aggregate), (List<DomainEvent>) events);
    }

    /**
     * 指定した集約およびイベントリストを使用して、ドメインイベントを発行します。
     *
     * @param aggregate イベントが関連する集約
     * @param events 発行するイベントリスト
     * @param eventTypeName イベントタイプ名
     */
    @SuppressWarnings("unchecked")
    public void publish(A aggregate, List<E> events, String eventTypeName) {
        eventPublisher.publish(aggregateTypeName, idSupplier.apply(aggregate), (List<DomainEvent>) events, eventTypeName);
    }
}
