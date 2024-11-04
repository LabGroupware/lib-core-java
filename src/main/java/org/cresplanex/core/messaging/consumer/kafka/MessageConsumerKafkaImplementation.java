package org.cresplanex.core.messaging.consumer.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.common.kafka.consumer.KafkaSubscription;
import org.cresplanex.core.common.kafka.consumer.CoreKafkaMessageConsumer;
import org.cresplanex.core.messaging.common.MessageImpl;
import org.cresplanex.core.messaging.consumer.MessageConsumerImplementation;
import org.cresplanex.core.messaging.consumer.MessageHandler;
import org.cresplanex.core.messaging.consumer.MessageSubscription;

/**
 * Kafkaを利用してメッセージを消費するための実装クラスです。
 * 指定されたチャンネルに対して購読を行い、メッセージが受信されると
 * 指定されたハンドラで処理します。
 */
public class MessageConsumerKafkaImplementation implements MessageConsumerImplementation {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CoreKafkaMessageConsumer messageConsumerKafka;

    /**
     * 指定されたCoreKafkaMessageConsumerを使用して、MessageConsumerKafkaのインスタンスを初期化します。
     *
     * @param coreKafkaMessageConsumer メッセージを消費するためのCoreKafkaMessageConsumerのインスタンス
     */
    public MessageConsumerKafkaImplementation(CoreKafkaMessageConsumer coreKafkaMessageConsumer) {
        this.messageConsumerKafka = coreKafkaMessageConsumer;
    }

    /**
     * 指定されたチャンネルに対してメッセージの購読を開始します。
     * メッセージが受信されると、指定されたハンドラで処理を行います。
     *
     * @param subscriberId 購読者のID
     * @param channels 購読対象のチャンネルのセット
     * @param handler メッセージを処理するためのハンドラ
     * @return メッセージの購読を管理するMessageSubscription
     */
    @Override
    public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {
        logger.info("Subscribing: subscriberId = {}, channels = {}", subscriberId, channels);

//        KafkaSubscription subscription = messageConsumerKafka.subscribe(subscriberId,
//                channels, message -> handler.accept(message.getPayloadContent()));

        KafkaSubscription subscription = messageConsumerKafka.subscribe(subscriberId,
                channels, message -> {
                    logger.info("Received message: {}", message);
                    MessageImpl messageImpl = message.getPayloadContent();
                    logger.info("Received message: {}", messageImpl);
                    handler.accept(messageImpl);
                });

        logger.info("Subscribed: subscriberId = {}, channels = {}", subscriberId, channels);

        return subscription::close;
    }

    /**
     * このコンシューマの一意のIDを取得します。
     *
     * @return コンシューマのID
     */
    @Override
    public String getId() {
        return messageConsumerKafka.getId();
    }

    /**
     * コンシューマを閉じ、リソースを解放します。
     */
    @Override
    public void close() {
        logger.info("Closing consumer");
        messageConsumerKafka.close();
        logger.info("Closed consumer");
    }
}
