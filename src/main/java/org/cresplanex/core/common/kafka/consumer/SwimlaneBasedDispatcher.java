package org.cresplanex.core.common.kafka.consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import org.apache.kafka.common.TopicPartition;
import org.cresplanex.core.common.kafka.common.RawKafkaMessage;
import org.cresplanex.core.common.kafka.consumer.swimlanemap.TopicPartitionToSwimlaneMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メッセージをスイムレーンごとに振り分けて処理するためのディスパッチャです。 スイムレーンごとに別々の処理キューを持ち、メッセージの分散処理を行います。
 */
public class SwimlaneBasedDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(SwimlaneBasedDispatcher.class);

    // スイムレーンIDとスイムレーンディスパッチャのマッピング
    private final ConcurrentHashMap<Integer, SwimlaneDispatcher> map = new ConcurrentHashMap<>();
    private final Executor executor;
    private final String subscriberId;
    private final TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping;

    /**
     * 新しいSwimlaneBasedDispatcherを作成します。
     *
     * @param subscriberId サブスクライバID
     * @param executor スレッドプールの実行者
     * @param partitionToSwimLaneMapping パーティションからスイムレーンへのマッピング
     */
    public SwimlaneBasedDispatcher(String subscriberId, Executor executor, TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping) {
        this.subscriberId = subscriberId;
        this.executor = executor;
        this.partitionToSwimLaneMapping = partitionToSwimLaneMapping;
    }

    /**
     * メッセージを適切なスイムレーンに振り分け、指定されたコンシューマで処理を開始します。
     *
     * @param message 処理対象のメッセージ
     * @param topicPartition メッセージのトピックパーティション
     * @param target メッセージの処理を行うコンシューマ
     * @return スイムレーンのバックログ情報
     */
    public SwimlaneDispatcherBacklog dispatch(RawKafkaMessage message, TopicPartition topicPartition, Consumer<RawKafkaMessage> target) {
        Integer swimlane = partitionToSwimLaneMapping.toSwimlane(topicPartition, message.getMessageKey()); // スイムレーンの決定
        logger.trace("Dispatching to swimlane {} for {}", swimlane, message);
        SwimlaneDispatcher swimlaneDispatcher = getOrCreate(swimlane); // スイムレーンIDからスイムレーンディスパッチャの取得・ない場合の作成
        return swimlaneDispatcher.dispatch(message, target); // スイムレーンディスパッチャに処理を委譲し, バックログ情報を返却, バックログ情報はスイムレーンディスパッチャのキューの状態を示す
    }

    /**
     * 指定されたスイムレーンに対応するSwimlaneDispatcherを取得、または新規作成します。
     *
     * @param swimlane スイムレーンのインデックス
     * @return スイムレーンディスパッチャ
     */
    private SwimlaneDispatcher getOrCreate(Integer swimlane) {
        SwimlaneDispatcher swimlaneDispatcher = map.get(swimlane);
        if (swimlaneDispatcher == null) {
            logger.trace("No dispatcher for {} {}. Attempting to create", subscriberId, swimlane);
            swimlaneDispatcher = new SwimlaneDispatcher(subscriberId, swimlane, executor);
            SwimlaneDispatcher r = map.putIfAbsent(swimlane, swimlaneDispatcher);
            if (r != null) { // 他のスレッドが先に生成していた場合
                logger.trace("Using concurrently created SwimlaneDispatcher for {} {}", subscriberId, swimlane);
                swimlaneDispatcher = r;
            } else {
                logger.trace("Using newly created SwimlaneDispatcher for {} {}", subscriberId, swimlane);
            }
        }
        return swimlaneDispatcher;
    }
}
