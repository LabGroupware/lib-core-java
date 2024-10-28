package org.cresplanex.core.common.jdbc;

import static java.lang.Math.abs;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * アウトボックステーブルとそのパーティション情報の管理仕様を提供するクラス。
 * <p>
 * このクラスは、テーブル数とパーティション数に基づき、メッセージキーからアウトボックステーブルと パーティションを計算します。
 * </p>
 */
public class OutboxPartitioningSpec {

    /**
     * 使用するアウトボックステーブル数
     */
    private final Integer outboxTables;

    /**
     * 各アウトボックステーブルに設定するパーティション数
     */
    private final Integer outboxTablePartitions;

    /**
     * デフォルトのパーティション仕様インスタンス
     */
    public final static OutboxPartitioningSpec DEFAULT = new OutboxPartitioningSpec(null, null);

    /**
     * 指定されたアウトボックステーブル数とパーティション数で {@link OutboxPartitioningSpec} を構築します。
     *
     * @param outboxTables アウトボックステーブルの数
     * @param outboxTablePartitions 各アウトボックステーブルに設定するパーティション数
     */
    public OutboxPartitioningSpec(Integer outboxTables, Integer outboxTablePartitions) {
        this.outboxTables = outboxTables;
        this.outboxTablePartitions = outboxTablePartitions;
    }

    /**
     * 宛先とメッセージキーに基づき、アウトボックステーブルサフィックスとパーティション番号を取得します。
     *
     * @param destination メッセージの宛先
     * @param messageKey メッセージのキー
     * @return {@link OutboxPartitionValues} オブジェクト（テーブルサフィックスとパーティション番号を含む）
     */
    public OutboxPartitionValues outboxTableValues(String destination, String messageKey) {
        Integer hash = abs(Objects.hash(destination, messageKey));

        Integer outboxTableSuffix = nullOrOne(outboxTables) || messageKey == null ? null : hash % outboxTables;
        Integer messagePartition = nullOrOne(outboxTablePartitions) || messageKey == null ? null : hash % outboxTablePartitions;

        return new OutboxPartitionValues(outboxTableSuffix, messagePartition);
    }

    /**
     * 値が null または 1 であるかを判定します。
     *
     * @param x 判定する整数値
     * @return x が null または 1 の場合に true
     */
    private boolean nullOrOne(Integer x) {
        return x == null || x == 1;
    }

    /**
     * 設定されているアウトボックステーブルサフィックスのリストを返します。
     *
     * @return {@link OutboxTableSuffix} のリスト
     */
    public List<OutboxTableSuffix> outboxTableSuffixes() {
        if (nullOrOne(outboxTables)) {
            return Collections.singletonList(new OutboxTableSuffix(null)); 
        }else {
            return IntStream.range(0, outboxTables).mapToObj(OutboxTableSuffix::new).collect(Collectors.toList());
        }
    }

    /**
     * 指定されたアウトボックステーブル数で新しい {@link OutboxPartitioningSpec} を生成します。
     *
     * @param outboxTables 新しいアウトボックステーブル数
     * @return 新しいアウトボックスパーティション仕様
     */
    public OutboxPartitioningSpec withOutboxTables(int outboxTables) {
        return new OutboxPartitioningSpec(outboxTables, this.outboxTablePartitions);
    }

    /**
     * 指定されたテーブルパーティション数で新しい {@link OutboxPartitioningSpec} を生成します。
     *
     * @param outboxTablePartitions 新しいテーブルパーティション数
     * @return 新しいアウトボックスパーティション仕様
     */
    public OutboxPartitioningSpec withTablePartitions(int outboxTablePartitions) {
        return new OutboxPartitioningSpec(this.outboxTables, outboxTablePartitions);
    }
}
