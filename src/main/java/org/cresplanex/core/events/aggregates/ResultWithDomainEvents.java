package org.cresplanex.core.events.aggregates;

import java.util.Arrays;
import java.util.List;
import org.cresplanex.core.events.common.DomainEvent;

/**
 * ドメインイベントを伴う操作の結果を表すクラスです。
 *
 * @param <A> 操作の結果タイプ
 * @param <E> ドメインイベントのタイプ
 */
public class ResultWithDomainEvents<A, E extends DomainEvent> {

    /**
     * 操作の結果オブジェクト
     */
    public final A result;

    /**
     * 発生したドメインイベントのリスト
     */
    public final List<E> events;

    /**
     * 操作結果とイベントリストを持つインスタンスを生成します。
     *
     * @param result 操作の結果
     * @param events ドメインイベントのリスト
     */
    public ResultWithDomainEvents(A result, List<E> events) {
        this.result = result;
        this.events = events;
    }

    /**
     * 操作結果と可変個のイベントを持つインスタンスを生成します。
     *
     * @param result 操作の結果
     * @param events ドメインイベントの可変引数
     */
    @SafeVarargs
    public ResultWithDomainEvents(A result, E... events) {
        this.result = result;
        this.events = Arrays.asList(events);
    }
}
