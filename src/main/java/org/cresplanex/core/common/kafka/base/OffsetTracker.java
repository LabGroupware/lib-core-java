package org.cresplanex.core.common.kafka.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * メッセージオフセットの管理クラス。
 * <p>
 * 各TopicPartitionにおける処理中および処理済みのメッセージオフセットのトラッキングを行い、 コミット可能なオフセットを管理します。
 * </p>
 */
public class OffsetTracker {

    private final Map<TopicPartition, TopicPartitionOffsets> state = new HashMap<>();

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("state", state)
                .toString();
    }

    /**
     * 指定したTopicPartitionのオフセット状態を取得します。 該当する状態がない場合、新しい状態を作成して返します。
     *
     * @param topicPartition トピックパーティション
     * @return TopicPartitionOffsetsインスタンス
     */
    private TopicPartitionOffsets fetch(TopicPartition topicPartition) {
        TopicPartitionOffsets tpo = state.get(topicPartition);
        if (tpo == null) {
            tpo = new TopicPartitionOffsets();
            state.put(topicPartition, tpo);
        }
        return tpo;
    }

    /**
     * 指定されたトピックパーティションとオフセットを「処理中」として記録します。
     *
     * @param topicPartition トピックパーティション
     * @param offset オフセット
     */
    void noteUnprocessed(TopicPartition topicPartition, long offset) {
        fetch(topicPartition).noteUnprocessed(offset);
    }

    /**
     * 指定されたトピックパーティションとオフセットを「処理完了」として記録します。
     *
     * @param topicPartition トピックパーティション
     * @param offset オフセット
     */
    void noteProcessed(TopicPartition topicPartition, long offset) {
        fetch(topicPartition).noteProcessed(offset);
    }

    private final int OFFSET_ADJUSTMENT = 1;

    /**
     * コミット可能なオフセットとメタデータのマップを返します。
     *
     * @return コミット可能なオフセットのマップ
     */
    public Map<TopicPartition, OffsetAndMetadata> offsetsToCommit() {
        Map<TopicPartition, OffsetAndMetadata> result = new HashMap<>();
        state.forEach((tp, tpo) -> tpo.offsetToCommit().ifPresent(offset -> result.put(tp, new OffsetAndMetadata(offset + OFFSET_ADJUSTMENT, ""))));
        return result;
    }

    /**
     * 指定されたコミット済みオフセットを状態に反映します。
     *
     * @param offsetsToCommit コミット済みオフセットのマップ
     */
    public void noteOffsetsCommitted(Map<TopicPartition, OffsetAndMetadata> offsetsToCommit) {
        offsetsToCommit.forEach((tp, om) -> {
            fetch(tp).noteOffsetCommitted(om.offset() - OFFSET_ADJUSTMENT);
        });
    }

    /**
     * 保留中のオフセットをトピックパーティションごとに取得します。
     *
     * @return 保留中オフセットのマップ
     */
    public Map<TopicPartition, Set<Long>> getPending() {
        Map<TopicPartition, Set<Long>> result = new HashMap<>();
        state.forEach((tp, tpo) -> {
            Set<Long> pending = tpo.getPending();
            if (!pending.isEmpty()) {
                result.put(tp, pending);
            }
        });
        return result;
    }
}
