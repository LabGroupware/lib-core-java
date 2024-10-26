package org.cresplanex.core.messaging.kafka.common;

public class CoreKafkaConfigurationProperties {

    private final String bootstrapServers;
    private final long connectionValidationTimeout;

    public CoreKafkaConfigurationProperties(String bootstrapServers, long connectionValidationTimeout) {
        this.bootstrapServers = bootstrapServers;
        this.connectionValidationTimeout = connectionValidationTimeout;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public long getConnectionValidationTimeout() {
        return connectionValidationTimeout;
    }
}
