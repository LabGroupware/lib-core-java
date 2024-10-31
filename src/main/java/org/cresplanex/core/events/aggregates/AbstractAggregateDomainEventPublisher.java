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
    private final Class<A> aggregateType;

    /**
     * コンストラクタ。集約イベントパブリッシャーの基本的な情報を設定します。
     *
     * @param eventPublisher イベントを発行するためのインスタンス
     * @param aggregateType 集約のクラス型
     * @param idSupplier 集約からIDを取得するための関数
     */
    protected AbstractAggregateDomainEventPublisher(DomainEventPublisher eventPublisher,
            Class<A> aggregateType,
            Function<A, Object> idSupplier) {
        this.eventPublisher = eventPublisher;
        this.aggregateType = aggregateType;
        this.idSupplier = idSupplier;
    }

    /**
     * 集約のクラス型を返します。
     *
     * @return 集約のクラス型
     */
    public Class<A> getAggregateType() {
        return aggregateType;
    }

    /**
     * 指定した集約およびイベントリストを使用して、ドメインイベントを発行します。
     *
     * @param aggregate イベントが関連する集約
     * @param events 発行するイベントリスト
     */
    @SuppressWarnings("unchecked")
    public void publish(A aggregate, List<E> events) {
        eventPublisher.publish(aggregateType, idSupplier.apply(aggregate), (List<DomainEvent>) events);
    }
}
