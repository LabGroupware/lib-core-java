package org.cresplanex.core.messaging.consumer;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.cresplanex.core.messaging.consumer.decorator.DecoratedMessageHandlerFactory;
import org.cresplanex.core.messaging.consumer.subscribermap.SubscriberMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メッセージコンシューマの実装クラス。 チャンネルマッピングやデコレータを使用してメッセージを購読し、処理する機能を提供します。
 */
public final class MessageConsumerImpl implements MessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * チャンネル名の変換を行うためのマッピングインターフェース。
     */
    private final ChannelMapping channelMapping;

    /**
     * メッセージの購読および処理の基本的な実装。
     */
    private final MessageConsumerImplementation target;

    /**
     * メッセージハンドラのデコレータを生成するファクトリ。
     */
    private final DecoratedMessageHandlerFactory decoratedMessageHandlerFactory;

    /**
     * サブスクライバIDの内部/外部マッピングを管理するインターフェース。
     */
    private final SubscriberMapping subscriberMapping;

    /**
     * コンストラクタ。 必要な依存オブジェクトを初期化します。
     *
     * @param channelMapping チャンネル名のマッピングオブジェクト
     * @param target メッセージコンシューマの基本的な実装
     * @param decoratedMessageHandlerFactory メッセージハンドラのデコレータファクトリ
     * @param subscriberMapping サブスクライバIDのマッピングオブジェクト
     */
    public MessageConsumerImpl(ChannelMapping channelMapping,
            MessageConsumerImplementation target,
            DecoratedMessageHandlerFactory decoratedMessageHandlerFactory,
            SubscriberMapping subscriberMapping) {
        this.channelMapping = channelMapping;
        this.target = target;
        this.decoratedMessageHandlerFactory = decoratedMessageHandlerFactory;
        this.subscriberMapping = subscriberMapping;
    }

    /**
     * 指定されたサブスクライバIDとチャンネルでメッセージを購読します。 メッセージハンドラは、デコレータファクトリを使用してデコレートされます。
     *
     * @param subscriberId サブスクライバのID
     * @param channels 購読するチャンネルのセット
     * @param handler メッセージの処理を行うハンドラ
     * @return メッセージ購読の情報を持つMessageSubscriptionオブジェクト
     */
    @Override
    public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {
        logger.info("Subscribing: subscriberId = {}, channels = {}", subscriberId, channels);

        Consumer<SubscriberIdAndMessage> decoratedHandler = decoratedMessageHandlerFactory.decorate(handler);

        MessageSubscription messageSubscription = target.subscribe(subscriberMapping.toExternal(subscriberId),
                channels.stream().map(channelMapping::transform).collect(Collectors.toSet()),
                message -> {
            try {
                decoratedHandler.accept(new SubscriberIdAndMessage(subscriberId, message));
            } catch (Exception e) {
                logger.error("Got exception: ", e);
                throw e;
            }
        });

        logger.info("Subscribed: subscriberId = {}, channels = {}", subscriberId, channels);

        return messageSubscription;
    }

    /**
     * メッセージコンシューマの一意のIDを取得します。
     *
     * @return コンシューマのIDを示す文字列
     */
    @Override
    public String getId() {
        return target.getId();
    }

    /**
     * コンシューマを閉じ、リソースを解放します。
     */
    @Override
    public void close() {
        logger.info("Closing consumer");

        target.close();

        logger.info("Closed consumer");
    }

}
