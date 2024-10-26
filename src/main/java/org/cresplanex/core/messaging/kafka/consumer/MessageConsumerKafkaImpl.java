package org.cresplanex.core.messaging.kafka.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.cresplanex.core.messaging.kafka.basic.consumer.CoreKafkaConsumer;
import org.cresplanex.core.messaging.kafka.basic.consumer.CoreKafkaConsumerConfigurationProperties;
import org.cresplanex.core.messaging.kafka.basic.consumer.CoreKafkaConsumerMessageHandler;
import org.cresplanex.core.messaging.kafka.basic.consumer.KafkaConsumerFactory;
import org.cresplanex.core.messaging.kafka.common.CoreBinaryMessageEncoding;
import org.cresplanex.core.messaging.kafka.common.CoreKafkaMultiMessage;
import org.cresplanex.core.messaging.kafka.common.CoreKafkaMultiMessageConverter;
import org.cresplanex.core.messaging.partitionmanagement.CommonMessageConsumer;
// import java.util.stream.Collectors;

public class MessageConsumerKafkaImpl implements CommonMessageConsumer {

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String id = UUID.randomUUID().toString();

    private final String bootstrapServers;
    private final List<CoreKafkaConsumer> consumers = new ArrayList<>();
    private final CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties;
    private final KafkaConsumerFactory kafkaConsumerFactory;
    private final CoreKafkaMultiMessageConverter coreKafkaMultiMessageConverter = new CoreKafkaMultiMessageConverter();
    private final TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping;

    public MessageConsumerKafkaImpl(String bootstrapServers,
            CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties,
            KafkaConsumerFactory kafkaConsumerFactory, TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping) {
        this.bootstrapServers = bootstrapServers;
        this.coreKafkaConsumerConfigurationProperties = coreKafkaConsumerConfigurationProperties;
        this.kafkaConsumerFactory = kafkaConsumerFactory;
        this.partitionToSwimLaneMapping = partitionToSwimLaneMapping;
    }

    public KafkaSubscription subscribe(String subscriberId, Set<String> channels, KafkaMessageHandler handler) {
        return subscribeWithReactiveHandler(subscriberId, channels, kafkaMessage -> {
            handler.accept(kafkaMessage);
            return CompletableFuture.completedFuture(null);
        });
    }

    public KafkaSubscription subscribeWithReactiveHandler(String subscriberId, Set<String> channels, ReactiveKafkaMessageHandler handler) {

        SwimlaneBasedDispatcher swimlaneBasedDispatcher = new SwimlaneBasedDispatcher(subscriberId, Executors.newCachedThreadPool(), partitionToSwimLaneMapping);

        CoreKafkaConsumerMessageHandler kcHandler = (record, callback) -> {
            if (coreKafkaMultiMessageConverter.isMultiMessage(record.value())) {
                return handleBatch(record, swimlaneBasedDispatcher, callback, handler);
            } else {
                return swimlaneBasedDispatcher.dispatch(new RawKafkaMessage(record.key(), record.value()), new TopicPartition(record.topic(), record.partition()), message -> handle(message, callback, handler));
            }
        };

        CoreKafkaConsumer kc = new CoreKafkaConsumer(subscriberId,
                kcHandler,
                new ArrayList<>(channels),
                bootstrapServers,
                coreKafkaConsumerConfigurationProperties,
                kafkaConsumerFactory);

        consumers.add(kc);

        kc.start();

        return new KafkaSubscription(() -> {
            kc.stop();
            consumers.remove(kc);
        });
    }

    private SwimlaneDispatcherBacklog handleBatch(ConsumerRecord<String, byte[]> record,
            SwimlaneBasedDispatcher swimlaneBasedDispatcher,
            BiConsumer<Void, Throwable> callback,
            ReactiveKafkaMessageHandler handler) {
        return coreKafkaMultiMessageConverter
                .convertBytesToMessages(record.value())
                .getMessages()
                .stream()
                .map(CoreKafkaMultiMessage::getValue)
                .map(KafkaMessage::new)
                .map(kafkaMessage
                        -> swimlaneBasedDispatcher.dispatch(new RawKafkaMessage(record.key(), record.value()), new TopicPartition(record.topic(), record.partition()), message -> handle(message, callback, handler)))
                .reduce((first, second) -> second)
                .get();
    }

    private void handle(RawKafkaMessage message, BiConsumer<Void, Throwable> callback, ReactiveKafkaMessageHandler kafkaMessageHandler) {
        try {
            kafkaMessageHandler
                    .apply(new KafkaMessage(CoreBinaryMessageEncoding.bytesToString(message.getPayload())))
                    .get();
        } catch (RuntimeException e) {
            callback.accept(null, e);
            throw e;
        } catch (InterruptedException | ExecutionException e) {
            callback.accept(null, e);
            throw new RuntimeException(e);
        }
        callback.accept(null, null);

        // try {
        //     kafkaMessageHandler
        //             .apply(new KafkaMessage(CoreBinaryMessageEncoding.bytesToString(message.getPayload())))
        //             .get();
        // } catch (RuntimeException e) {
        //     callback.accept(null, e);
        //     throw e;
        // } catch (Throwable e) {
        //     callback.accept(null, e);
        //     throw new RuntimeException(e);
        // }
        // callback.accept(null, null);
    }

    @Override
    public void close() {
        consumers.forEach(CoreKafkaConsumer::stop);
    }

    public String getId() {
        return id;
    }
}
