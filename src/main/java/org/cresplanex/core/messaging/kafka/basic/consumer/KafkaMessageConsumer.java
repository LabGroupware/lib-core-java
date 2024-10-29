package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

/**
 * Kafkaメッセージコンシューマのインターフェース。
 * <p>
 * このインターフェースは、Kafkaメッセージの消費に関連する操作を提供します。
 * </p>
 */
public interface KafkaMessageConsumer {

    /**
     * 指定されたトピックパーティションをコンシューマに割り当てます。
     *
     * @param topicPartitions 割り当てるトピックパーティションのコレクション
     */
    void assign(Collection<TopicPartition> topicPartitions);

    /**
     * トピックパーティションの最後にシークします。
     *
     * @param topicPartitions 対象のトピックパーティション
     */
    void seekToEnd(Collection<TopicPartition> topicPartitions);

    /**
     * 指定されたパーティションの現在のポジションを取得します。
     *
     * @param topicPartition 対象のトピックパーティション
     * @return 現在のオフセット位置
     */
    long position(TopicPartition topicPartition);

    /**
     * 指定されたパーティションの位置をシークします。
     *
     * @param topicPartition 対象のトピックパーティション
     * @param position シーク先の位置
     */
    void seek(TopicPartition topicPartition, long position);

    /**
     * 指定されたトピックにサブスクライブします。
     *
     * @param topics サブスクライブするトピックのリスト
     */
    void subscribe(List<String> topics);

    /**
     * 指定されたオフセットをコミットします。
     *
     * @param offsets コミットするオフセットのマップ
     */
    void commitOffsets(Map<TopicPartition, OffsetAndMetadata> offsets);

    /**
     * 指定されたトピックのパーティション情報を取得します。
     *
     * @param topic トピック名
     * @return パーティション情報のリスト
     */
    List<PartitionInfo> partitionsFor(String topic);

    /**
     * 指定された期間にわたってポーリングを行います。
     *
     * @param duration ポーリング期間
     * @return 取得したレコード
     */
    ConsumerRecords<String, byte[]> poll(Duration duration);

    /**
     * 指定されたパーティションを一時停止します。
     *
     * @param partitions 一時停止するパーティションのセット
     */
    void pause(Set<TopicPartition> partitions);

    /**
     * 指定されたパーティションを再開します。
     *
     * @param partitions 再開するパーティションのセット
     */
    void resume(Set<TopicPartition> partitions);

    /**
     * コンシューマを閉じます。
     */
    void close();

    /**
     * 指定された期間内にコンシューマを閉じます。
     *
     * @param duration 閉じるまでの期間
     */
    void close(Duration duration);

    /**
     * トピックにサブスクライブし、リバランスコールバックを設定します。
     *
     * @param topics サブスクライブするトピックのコレクション
     * @param callback リバランスコールバック
     */
    void subscribe(Collection<String> topics, ConsumerRebalanceListener callback);
}
