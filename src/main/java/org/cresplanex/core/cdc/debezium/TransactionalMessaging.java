package org.cresplanex.core.cdc.debezium;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

import static org.apache.kafka.connect.transforms.util.Requirements.requireStruct;

public class TransactionalMessaging<R extends ConnectRecord<R>> implements Transformation<R> {
    @Override
    public R apply(R record) {
        if (record.value() == null) {
            return record;
        }

        Struct value = requireStruct(record.value(), "Record value should be struct.");

        // create以外の操作は無視
        String op = value.getString("op");
        if (op == null || !op.equals("c")) {
            return record;
        }

        Object after = value.get("after");
        if (after == null) {
            return record;
        }

        Struct afterValue = requireStruct(after, "After value should be struct.");

        // destinationを動的に設定
        String destination = afterValue.getString("destination");
        if (destination == null) {
            destination = record.topic();
        }

        return record.newRecord(
                destination,
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                record.valueSchema().field("after").schema(),
                after,
                record.timestamp()
        );
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}