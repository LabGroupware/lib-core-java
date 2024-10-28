package org.cresplanex.core.common.jdbc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * アウトボックスのパーティション情報を保持するクラス。
 * <p>
 * このクラスは、アウトボックステーブルのサフィックスとメッセージパーティション情報を格納します。
 * </p>
 */
public class OutboxPartitionValues {

    /**
     * アウトボックステーブルのサフィックス情報
     */
    public final OutboxTableSuffix outboxTableSuffix;

    /**
     * メッセージのパーティション番号
     */
    public final Integer messagePartition;

    /**
     * 指定されたテーブルサフィックスとメッセージパーティションで {@link OutboxPartitionValues} オブジェクトを構築します。
     *
     * @param outboxTableSuffix テーブルサフィックス
     * @param messagePartition メッセージパーティション
     */
    public OutboxPartitionValues(Integer outboxTableSuffix, Integer messagePartition) {
        this.outboxTableSuffix = new OutboxTableSuffix(outboxTableSuffix);
        this.messagePartition = messagePartition;
    }

    /**
     * このオブジェクトと指定されたオブジェクトが等しいかを判定します。
     * <p>
     * 同じクラスで、かつ `outboxTableSuffix` と `messagePartition` が等しい場合に true を返します。
     * </p>
     *
     * @param o 比較対象のオブジェクト
     * @return オブジェクトが等しい場合は true、そうでない場合は false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutboxPartitionValues that = (OutboxPartitionValues) o;

        return new EqualsBuilder()
                .append(outboxTableSuffix.suffix, that.outboxTableSuffix.suffix)
                .append(messagePartition, that.messagePartition)
                .isEquals();
    }

    /**
     * このオブジェクトの文字列表現を返します。
     *
     * @return このオブジェクトの文字列形式
     */
    @Override
    public String toString() {
        return "OutboxPartitionValues{"
                + "outboxTableSuffix='" + outboxTableSuffix.suffix + '\''
                + ", messagePartition=" + messagePartition
                + '}';
    }

    /**
     * このオブジェクトのハッシュコードを返します。
     *
     * @return `outboxTableSuffix` と `messagePartition` に基づくハッシュコード
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(outboxTableSuffix.suffix)
                .append(messagePartition)
                .toHashCode();
    }
}
