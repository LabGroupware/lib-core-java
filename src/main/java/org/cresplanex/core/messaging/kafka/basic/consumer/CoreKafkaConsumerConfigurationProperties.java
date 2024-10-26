package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashMap;
import java.util.Map;

public class CoreKafkaConsumerConfigurationProperties {

    private Map<String, String> properties = new HashMap<>();

    private BackPressureConfig backPressure = new BackPressureConfig();
    private long pollTimeout;

    public BackPressureConfig getBackPressure() {
        return backPressure;
    }

    public void setBackPressure(BackPressureConfig backPressure) {
        this.backPressure = backPressure;
    }

    public long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public CoreKafkaConsumerConfigurationProperties() {
    }

    public CoreKafkaConsumerConfigurationProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public static CoreKafkaConsumerConfigurationProperties empty() {
        return new CoreKafkaConsumerConfigurationProperties();
    }

}
