package org.cresplanex.core.common.jdbc;

import static java.lang.Math.abs;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OutboxPartitioningSpec {
    private final Integer outboxTables;
    private final Integer outboxTablePartitions;

    public final static OutboxPartitioningSpec DEFAULT = new OutboxPartitioningSpec(null, null);

    public OutboxPartitioningSpec(Integer outboxTables, Integer outboxTablePartitions) {
        this.outboxTables = outboxTables;
        this.outboxTablePartitions = outboxTablePartitions;
    }

    public OutboxPartitionValues outboxTableValues(String destination, String messageKey) {
        Integer hash = abs(Objects.hash(destination, messageKey));

        Integer outboxTableSuffix = nullOrOne(outboxTables) || messageKey == null ? null : hash % outboxTables;
        Integer messagePartition = nullOrOne(outboxTablePartitions) || messageKey == null ? null : hash % outboxTablePartitions;

        return new OutboxPartitionValues(outboxTableSuffix, messagePartition);
    }

    private boolean nullOrOne(Integer x) {
        return x == null || x == 1;
    }

    public List<OutboxTableSuffix> outboxTableSuffixes() {
        if (nullOrOne(outboxTables))
            return Collections.singletonList(new OutboxTableSuffix(null));
        else {
            return IntStream.range(0, outboxTables).mapToObj(OutboxTableSuffix::new).collect(Collectors.toList());
        }
    }

    public OutboxPartitioningSpec withOutboxTables(int outboxTables) {
        return new OutboxPartitioningSpec(outboxTables, this.outboxTablePartitions);
    }

    public OutboxPartitioningSpec withTablePartitions(int outboxTables) {
        return new OutboxPartitioningSpec(this.outboxTables, outboxTablePartitions);
    }
}
