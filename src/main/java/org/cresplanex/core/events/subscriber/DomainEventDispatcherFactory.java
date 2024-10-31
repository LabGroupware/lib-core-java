package org.cresplanex.core.events.subscriber;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

/**
 * ドメインイベントディスパッチャのファクトリクラスです。
 * 指定されたメッセージコンシューマとイベント名マッピングを使用して、イベントディスパッチャを生成します。
 */
public class DomainEventDispatcherFactory {

    protected MessageConsumer messageConsumer;
    protected DomainEventNameMapping domainEventNameMapping;

    /**
     * {@code DomainEventDispatcherFactory}のインスタンスを作成します。
     *
     * @param messageConsumer メッセージコンシューマ
     * @param domainEventNameMapping ドメインイベントの名前マッピング
     */
    public DomainEventDispatcherFactory(MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
        this.messageConsumer = messageConsumer;
        this.domainEventNameMapping = domainEventNameMapping;
    }

    /**
     * 指定されたIDとイベントハンドラで、新しいドメインイベントディスパッチャを作成し初期化します。
     *
     * @param eventDispatcherId ディスパッチャのID
     * @param domainEventHandlers 登録されたドメインイベントハンドラ
     * @return 初期化された{@code DomainEventDispatcher}インスタンス
     */
    public DomainEventDispatcher make(String eventDispatcherId, DomainEventHandlers domainEventHandlers) {
        DomainEventDispatcher domainEventDispatcher = new DomainEventDispatcher(eventDispatcherId, domainEventHandlers, messageConsumer, domainEventNameMapping);
        domainEventDispatcher.initialize();
        return domainEventDispatcher;
    }
}
