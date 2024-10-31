package org.cresplanex.core.events.subscriber;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.cresplanex.core.messaging.common.Message;

/**
 * ドメインイベントを処理するためのハンドラのリストを管理するクラスです。
 */
public class DomainEventHandlers {

    private final List<DomainEventHandler> handlers;

    /**
     * 指定されたドメインイベントハンドラのリストで{@code DomainEventHandlers}のインスタンスを生成します。
     *
     * @param handlers 管理するドメインイベントハンドラのリスト
     */
    public DomainEventHandlers(List<DomainEventHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * このハンドラが管理する集約タイプのセットを取得します。
     *
     * @return 集約タイプのセット
     */
    public Set<String> getAggregateTypesAndEvents() {
        return handlers.stream().map(DomainEventHandler::getAggregateType).collect(toSet());
    }

    /**
     * このハンドラが管理するすべてのドメインイベントハンドラを取得します。
     *
     * @return ドメインイベントハンドラのリスト
     */
    public List<DomainEventHandler> getHandlers() {
        return handlers;
    }

    /**
     * 指定されたメッセージを処理可能なハンドラを見つけます。
     *
     * @param message 処理対象のメッセージ
     * @return
     * 対応するドメインイベントハンドラが存在する場合は{@code Optional}で返し、それ以外の場合は空の{@code Optional}
     */
    public Optional<DomainEventHandler> findTargetMethod(Message message) {
        return handlers.stream().filter(h -> h.handles(message)).findFirst();
    }
}
