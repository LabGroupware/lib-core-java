package org.cresplanex.core.events.publisher;

import java.util.Arrays;
import java.util.List;
import org.cresplanex.core.events.common.DomainEvent;

/**
 * 操作結果と関連するドメインイベントを格納するクラスです。
 *
 * @param <T> 操作の結果のタイプ
 */
public class ResultWithEvents<T> {

    /**
     * 操作の結果
     */
    public final T result;

    /**
     * 発生したドメインイベントのリスト
     */
    public final List<DomainEvent> events;

    /**
     * 操作結果とイベントリストを持つインスタンスを生成します。
     *
     * @param result 操作の結果
     * @param events ドメインイベントのリスト
     */
    public ResultWithEvents(T result, List<DomainEvent> events) {
        this.result = result;
        this.events = events;
    }

    /**
     * 操作結果と可変個のイベントを持つインスタンスを生成します。
     *
     * @param result 操作の結果
     * @param events ドメインイベントの可変引数
     */
    public ResultWithEvents(T result, DomainEvent... events) {
        this.result = result;
        this.events = Arrays.asList(events);
    }
}
