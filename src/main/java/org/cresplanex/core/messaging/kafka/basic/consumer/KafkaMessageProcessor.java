package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafkaメッセージの処理と、成功したメッセージオフセットのトラッキングを行うクラス。
 * <p>
 * このクラスは、Kafkaからのメッセージを処理し、成功したメッセージのオフセットを管理して、 コミット可能なオフセットを提供します。
 * </p>
 */
public class KafkaMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String subscriberId;
    private final CoreKafkaConsumerMessageHandler handler;
    private final OffsetTracker offsetTracker = new OffsetTracker();

    private final BlockingQueue<ConsumerRecord<String, byte[]>> processedRecords = new LinkedBlockingQueue<>();
    private final AtomicReference<KafkaMessageProcessorFailedException> failed = new AtomicReference<>();

    private final Set<MessageConsumerBacklog> consumerBacklogs = new HashSet<>();

    /**
     * コンストラクタ。
     *
     * @param subscriberId サブスクライバID
     * @param handler メッセージハンドラ
     */
    public KafkaMessageProcessor(String subscriberId, CoreKafkaConsumerMessageHandler handler) {
        this.subscriberId = subscriberId;
        this.handler = handler;
    }

    /**
     * 指定されたレコードを処理します。
     *
     * @param record 処理対象のレコード
     */
    public void process(ConsumerRecord<String, byte[]> record) {
        throwFailureException();
        offsetTracker.noteUnprocessed(new TopicPartition(record.topic(), record.partition()), record.offset());
        MessageConsumerBacklog consumerBacklog = handler.apply(record, (result, t) -> {
            handleMessagingHandlingOutcome(record, t);
        });
        if (consumerBacklog != null) {
            consumerBacklogs.add(consumerBacklog);
        }
    }

    /**
     * メッセージ処理の結果をハンドリングします。
     *
     * @param record 処理対象のレコード
     * @param t 処理中に発生したエラー
     */
    private void handleMessagingHandlingOutcome(ConsumerRecord<String, byte[]> record, Throwable t) {
        if (t != null) {
            logger.error("Got exception: ", t);
            failed.set(new KafkaMessageProcessorFailedException(t));
        } else {
            logger.debug("Adding processed record to queue {} {}", subscriberId, record.offset());
            processedRecords.add(record);
        }
    }

    /**
     * 処理中に発生した失敗例外をスローします。
     *
     * @throws KafkaMessageProcessorFailedException 失敗例外
     */
    void throwFailureException() {
        if (failed.get() != null) {
            throw failed.get();
        }
    }

    /**
     * コミット可能なオフセットをマップとして返します。
     *
     * @return コミット可能なオフセットマップ
     */
    public Map<TopicPartition, OffsetAndMetadata> offsetsToCommit() {
        int count = 0;
        while (true) {
            ConsumerRecord<String, byte[]> record = processedRecords.poll();
            if (record == null) {
                break;
            }
            count++;
            offsetTracker.noteProcessed(new TopicPartition(record.topic(), record.partition()), record.offset());
        }
        logger.trace("removed {} {} processed records from queue", subscriberId, count);
        return offsetTracker.offsetsToCommit();
    }

    /**
     * コミット済みのオフセットを追跡状態に記録します。
     *
     * @param offsetsToCommit コミットしたオフセットのマップ
     */
    public void noteOffsetsCommitted(Map<TopicPartition, OffsetAndMetadata> offsetsToCommit) {
        offsetTracker.noteOffsetsCommitted(offsetsToCommit);
    }

    /**
     * 保留中のオフセットを取得します。
     *
     * @return OffsetTrackerインスタンス
     */
    public OffsetTracker getPending() {
        return offsetTracker;
    }

    /**
     * バックログのサイズを返します。
     *
     * @return バックログサイズ
     */
    public int backlog() {
        return consumerBacklogs.stream().mapToInt(MessageConsumerBacklog::size).sum();
    }
}
