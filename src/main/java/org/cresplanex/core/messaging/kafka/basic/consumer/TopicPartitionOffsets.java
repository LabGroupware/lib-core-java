package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 特定のTopicPartitionのオフセットを追跡するクラス。
 * <p>
 * このクラスは、処理中のオフセットと、処理が完了してコミット可能なオフセットの トラッキングを行います。
 * </p>
 */
public class TopicPartitionOffsets {

    /**
     * 処理中のオフセット
     */
    private SortedSet<Long> unprocessed = new TreeSet<>();

    /**
     * 処理が完了したオフセット
     */
    private Set<Long> processed = new HashSet<>();

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("unprocessed", unprocessed)
                .append("processed", processed)
                .toString();
    }

    /**
     * 指定されたオフセットを「処理中」として記録します。
     *
     * @param offset 記録するオフセット
     */
    public void noteUnprocessed(long offset) {
        unprocessed.add(offset);
    }

    /**
     * 指定されたオフセットを「処理完了」として記録します。
     *
     * @param offset 記録するオフセット
     */
    public void noteProcessed(long offset) {
        processed.add(offset);
    }

    /**
     * コミット可能な最大の処理済みオフセットを取得します。
     *
     * @return コミット可能なオフセット（存在しない場合は空のOptional）
     */
    Optional<Long> offsetToCommit() {
        Long result = null;
        for (long x : unprocessed) {
            if (processed.contains(x)) {
                result = x;
            } else {
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    /**
     * 指定されたオフセット以下の全てのオフセットを削除します。
     * ここで, コミットした最大オフセットを指定することで、
     * それよりも小さいオフセット(コミットしたすべてのオフセット)を削除します。
     *
     * @param offset コミットしたオフセット
     */
    public void noteOffsetCommitted(long offset) {
        unprocessed = new TreeSet<>(unprocessed.stream().filter(x -> x > offset).collect(Collectors.toList()));
        processed = processed.stream().filter(x -> x > offset).collect(Collectors.toSet());
    }

    /**
     * 保留中のオフセットを取得します。
     * 保留中のオフセットは、処理中のオフセットから処理済みのオフセットを除いたものです。
     *
     * @return 保留中のオフセットセット
     */
    public Set<Long> getPending() {
        Set<Long> result = new HashSet<>(unprocessed);
        result.removeAll(processed);
        return result;
    }
}
