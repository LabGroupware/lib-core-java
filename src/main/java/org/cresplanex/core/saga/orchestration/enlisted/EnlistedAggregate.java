package org.cresplanex.core.saga.orchestration.enlisted;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * エンリストされた集約（Aggregate）を表すクラス。 各集約はクラスとIDで一意に識別されます。
 */
public class EnlistedAggregate {

    private final Class<Object> aggregateClass;
    private final Object aggregateId;

    /**
     * 指定されたクラスとIDでエンリストされた集約を作成します。
     *
     * @param aggregateClass 集約のクラス
     * @param aggregateId 集約のID
     */
    public EnlistedAggregate(Class<Object> aggregateClass, Object aggregateId) {
        this.aggregateClass = aggregateClass;
        this.aggregateId = aggregateId;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 集約のクラスを取得します。
     *
     * @return 集約のクラス
     */
    public Class<Object> getAggregateClass() {
        return aggregateClass;
    }

    /**
     * 集約のIDを取得します。
     *
     * @return 集約のID
     */
    public Object getAggregateId() {
        return aggregateId;
    }
}
