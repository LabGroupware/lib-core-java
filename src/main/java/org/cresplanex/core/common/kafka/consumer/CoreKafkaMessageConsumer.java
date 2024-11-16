package org.cresplanex.core.common.kafka.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.cresplanex.core.common.kafka.base.CoreKafkaConsumer;
import org.cresplanex.core.common.kafka.base.CoreKafkaConsumerMessageHandler;
import org.cresplanex.core.common.kafka.common.CoreBinaryMessageEncoding;
import org.cresplanex.core.common.kafka.common.KafkaMessage;
import org.cresplanex.core.common.kafka.common.RawKafkaMessage;
import org.cresplanex.core.common.kafka.consumer.swimlanemap.TopicPartitionToSwimlaneMapping;
import org.cresplanex.core.common.kafka.lower.KafkaConsumerFactory;
import org.cresplanex.core.common.kafka.multi.CoreKafkaMultiMessage;
import org.cresplanex.core.common.kafka.multi.CoreKafkaMultiMessageConverter;
import org.cresplanex.core.common.kafka.property.CoreKafkaConsumerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafkaメッセージを消費し、処理するためのクラスです。このクラスは、複数のKafkaメッセージを管理し、 指定されたハンドラーでメッセージを処理します。
 */
public class CoreKafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(CoreKafkaMessageConsumer.class);
    private final String id = UUID.randomUUID().toString();
    private final String bootstrapServers;
    private final List<CoreKafkaConsumer> consumers = new ArrayList<>();
    private final CoreKafkaConsumerProperties coreKafkaConsumerProperties;
    private final KafkaConsumerFactory kafkaConsumerFactory;
    private final CoreKafkaMultiMessageConverter coreKafkaMultiMessageConverter = new CoreKafkaMultiMessageConverter();
    private final TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping;

    /**
     * Kafkaメッセージコンシューマーの設定を行うコンストラクタです。
     *
     * @param bootstrapServers Kafkaのブートストラップサーバー
     * @param coreKafkaConsumerProperties コンシューマーの詳細設定
     * @param kafkaConsumerFactory Kafkaコンシューマーの生成ファクトリ
     * @param partitionToSwimLaneMapping パーティションとスイムレーンのマッピング設定
     */
    public CoreKafkaMessageConsumer(String bootstrapServers,
            CoreKafkaConsumerProperties coreKafkaConsumerProperties,
            KafkaConsumerFactory kafkaConsumerFactory, TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping) {
        this.bootstrapServers = bootstrapServers;
        this.coreKafkaConsumerProperties = coreKafkaConsumerProperties;
        this.kafkaConsumerFactory = kafkaConsumerFactory;
        this.partitionToSwimLaneMapping = partitionToSwimLaneMapping;
    }

    /**
     * 指定されたチャンネルに対し、Kafkaメッセージハンドラーでサブスクライブします。
     *
     * @param subscriberId サブスクライバーの識別子
     * @param channels サブスクライブするチャンネルセット
     * @param handler メッセージを処理するKafkaメッセージハンドラー
     * @return KafkaSubscription インスタンス
     */
    public KafkaSubscription subscribe(String subscriberId, Set<String> channels, KafkaMessageHandler handler) {
        return subscribeWithReactiveHandler(subscriberId, channels, kafkaMessage -> {
            handler.accept(kafkaMessage);
            return CompletableFuture.completedFuture(null);
        });
    }

    /**
     * 指定されたチャンネルに対してリアクティブハンドラーでサブスクライブします。
     *
     * @param subscriberId サブスクライバーの識別子
     * @param channels サブスクライブするチャンネルセット
     * @param handler リアクティブにメッセージを処理するKafkaメッセージハンドラー
     * @return KafkaSubscription インスタンス
     */
    public KafkaSubscription subscribeWithReactiveHandler(String subscriberId, Set<String> channels, ReactiveKafkaMessageHandler handler) {

        SwimlaneBasedDispatcher swimlaneBasedDispatcher = new SwimlaneBasedDispatcher(subscriberId, Executors.newCachedThreadPool(), partitionToSwimLaneMapping);

        CoreKafkaConsumerMessageHandler kcHandler = (record, callback) -> {
            if (coreKafkaMultiMessageConverter.isMultiMessage(record.value())) {
                return handleBatch(record, swimlaneBasedDispatcher, callback, handler);
            } else {
                return swimlaneBasedDispatcher.dispatch(new RawKafkaMessage(record.key(), record.value()), new TopicPartition(record.topic(), record.partition()), message -> handle(message, callback, handler));
            }
        };

        CoreKafkaConsumer kc = new CoreKafkaConsumer(subscriberId,
                kcHandler,
                new ArrayList<>(channels),
                bootstrapServers,
                coreKafkaConsumerProperties,
                kafkaConsumerFactory);

        consumers.add(kc);
        kc.start();

        return new KafkaSubscription(() -> {
            kc.stop();
            consumers.remove(kc);
        });
    }

    /**
     * バッチでのメッセージを処理するためのメソッドです。
     *
     * @param record Kafkaからのコンシューマーレコード
     * @param swimlaneBasedDispatcher メッセージのディスパッチを管理するディスパッチャー
     * @param callback 処理結果のコールバック
     * @param handler リアクティブメッセージハンドラー
     * @return SwimlaneDispatcherBacklog インスタンス
     */
    private SwimlaneDispatcherBacklog handleBatch(ConsumerRecord<String, byte[]> record,
            SwimlaneBasedDispatcher swimlaneBasedDispatcher,
            BiConsumer<Void, Throwable> callback,
            ReactiveKafkaMessageHandler handler) {
        return coreKafkaMultiMessageConverter
                .convertBytesToMessages(record.value())
                .getMessages()
                .stream()
                .map(CoreKafkaMultiMessage::getValue)
                .map(KafkaMessage::new)
                .map(kafkaMessage
                        -> swimlaneBasedDispatcher.dispatch(new RawKafkaMessage(record.key(), record.value()), new TopicPartition(record.topic(), record.partition()), message -> handle(message, callback, handler)))
                .reduce((first, second) -> second)
                .get();
    }

    /**
     * 個々のメッセージをハンドラーで処理します。
     *
     * @param message 処理対象のRawKafkaMessage
     * @param callback 処理結果のコールバック
     * @param kafkaMessageHandler 処理を行うリアクティブメッセージハンドラー
     */
    private void handle(RawKafkaMessage message, BiConsumer<Void, Throwable> callback, ReactiveKafkaMessageHandler kafkaMessageHandler) {
        try {
            kafkaMessageHandler
                    .apply(new KafkaMessage(CoreBinaryMessageEncoding.bytesToString(message.getPayload())))
                    .get();
        } catch (RuntimeException e) {
            log.info("Error processing message==========================");
            callback.accept(null, e);
            throw e;
        } catch (InterruptedException | ExecutionException e) {
            callback.accept(null, e);
            throw new RuntimeException(e);
        }
        callback.accept(null, null);
    }

    /**
     * クローズ処理を行い、全てのコンシューマーを停止します。
     */
    public void close() {
        consumers.forEach(CoreKafkaConsumer::stop);
    }

    /**
     * このコンシューマーの一意なIDを取得します。
     *
     * @return このコンシューマーのID
     */
    public String getId() {
        return id;
    }
}
