package org.cresplanex.core.saga.orchestration;

import static java.util.Collections.singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.Failure;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.Success;
import org.cresplanex.core.commands.producer.CommandProducer;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.messaging.producer.MessageBuilder;
import org.cresplanex.core.saga.common.LockTarget;
import org.cresplanex.core.saga.common.SagaCommandHeaders;
import org.cresplanex.core.saga.common.SagaLockManager;
import org.cresplanex.core.saga.common.SagaReplyHeaders;
import org.cresplanex.core.saga.common.SagaUnlockCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SagaManagerImpl<Data>
        implements SagaManager<Data> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Saga<Data> saga;
    private SagaInstanceRepository sagaInstanceRepository;
    private CommandProducer commandProducer;
    private MessageConsumer messageConsumer;
    private SagaLockManager sagaLockManager;
    private SagaCommandProducer sagaCommandProducer;

    public SagaManagerImpl(Saga<Data> saga,
            SagaInstanceRepository sagaInstanceRepository,
            CommandProducer commandProducer,
            MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            SagaCommandProducer sagaCommandProducer) {
        this.saga = saga;
        this.sagaInstanceRepository = sagaInstanceRepository;
        this.commandProducer = commandProducer;
        this.messageConsumer = messageConsumer;
        this.sagaLockManager = sagaLockManager;
        this.sagaCommandProducer = sagaCommandProducer;
    }

    public void setSagaCommandProducer(SagaCommandProducer sagaCommandProducer) {
        this.sagaCommandProducer = sagaCommandProducer;
    }

    public void setSagaInstanceRepository(SagaInstanceRepository sagaInstanceRepository) {
        this.sagaInstanceRepository = sagaInstanceRepository;
    }

    public void setCommandProducer(CommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    public void setMessageConsumer(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void setSagaLockManager(SagaLockManager sagaLockManager) {
        this.sagaLockManager = sagaLockManager;
    }

    @Override
    public SagaInstance create(Data sagaData) {
        return create(sagaData, Optional.empty());
    }

    @Override
    public SagaInstance create(Data data, Class<?> targetClass, Object targetId) {
        return create(data, Optional.of(new LockTarget(targetClass, targetId).getTarget()));
    }

    @Override
    public SagaInstance create(Data sagaData, Optional<String> resource) {

        SagaInstance sagaInstance = new SagaInstance(getSagaType(),
                null,
                "????",
                null,
                SagaDataSerde.serializeSagaData(sagaData), new HashSet<>());

        sagaInstanceRepository.save(sagaInstance);

        String sagaId = sagaInstance.getId();

        saga.onStarting(sagaId, sagaData);

        resource.ifPresent(r -> {
            if (!sagaLockManager.claimLock(getSagaType(), sagaId, r)) {
                throw new RuntimeException("Cannot claim lock for resource");
            }
        });

        SagaActions<Data> actions = getStateDefinition().start(sagaData);

        actions.getLocalException().ifPresent(e -> {
            throw e;
        });

        processActions(saga.getSagaType(), sagaId, sagaInstance, sagaData, actions);

        return sagaInstance;
    }

    private void performEndStateActions(String sagaId, SagaInstance sagaInstance, boolean compensating, boolean failed, Data sagaData) {
        for (DestinationAndResource dr : sagaInstance.getDestinationsAndResources()) {
            Map<String, String> headers = new HashMap<>();
            headers.put(SagaCommandHeaders.SAGA_ID, sagaId);
            headers.put(SagaCommandHeaders.SAGA_TYPE, getSagaType()); // FTGO SagaCommandHandler failed without this but the OrdersAndCustomersIntegrationTest was fine?!?
            commandProducer.send(dr.getDestination(), dr.getResource(), new SagaUnlockCommand(), makeSagaReplyChannel(), headers);
        }

        if (failed) {
            saga.onSagaFailed(sagaId, sagaData);
        }
        if (compensating) {
            saga.onSagaRolledBack(sagaId, sagaData);
        } else {
            saga.onSagaCompletedSuccessfully(sagaId, sagaData);
        }

    }

    private SagaDefinition<Data> getStateDefinition() {
        SagaDefinition<Data> sm = saga.getSagaDefinition();

        if (sm == null) {
            throw new RuntimeException("state machine cannot be null");
        }

        return sm;
    }

    private String getSagaType() {
        return saga.getSagaType();
    }

    @Override
    public void subscribeToReplyChannel() {
        messageConsumer.subscribe(saga.getSagaType() + "-consumer", singleton(makeSagaReplyChannel()),
                this::handleMessage);
    }

    private String makeSagaReplyChannel() {
        return getSagaType() + "-reply";
    }

    public void handleMessage(Message message) {
        logger.debug("handle message invoked {}", message);
        if (message.hasHeader(SagaReplyHeaders.REPLY_SAGA_ID)) {
            handleReply(message);
        } else {
            logger.warn("Handle message doesn't know what to do with: {} ", message);
        }
    }

    private void handleReply(Message message) {

        if (!isReplyForThisSagaType(message)) {
            return;
        }

        logger.debug("Handle reply: {}", message);

        String sagaId = message.getRequiredHeader(SagaReplyHeaders.REPLY_SAGA_ID);
        String sagaType = message.getRequiredHeader(SagaReplyHeaders.REPLY_SAGA_TYPE);

        SagaInstance sagaInstance = sagaInstanceRepository.find(sagaType, sagaId);
        Data sagaData = SagaDataSerde.deserializeSagaData(sagaInstance.getSerializedSagaData());

        message.getHeader(SagaReplyHeaders.REPLY_LOCKED).ifPresent(lockedTarget -> {
            String destination = message.getRequiredHeader(CommandMessageHeaders.inReply(CommandMessageHeaders.DESTINATION));
            sagaInstance.addDestinationsAndResources(singleton(new DestinationAndResource(destination, lockedTarget)));
        });

        String currentState = sagaInstance.getStateName();

        logger.info("Current state={}", currentState);

        SagaActions<Data> actions = getStateDefinition().handleReply(sagaType, sagaId, currentState, sagaData, message);

        logger.info("Handled reply. Sending commands {}", actions.getCommands());

        processActions(sagaType, sagaId, sagaInstance, sagaData, actions);

    }

    private void processActions(String sagaType, String sagaId, SagaInstance sagaInstance, Data sagaData, SagaActions<Data> actions) {

        while (true) {

            if (actions.getLocalException().isPresent()) {

                actions = getStateDefinition().handleReply(sagaType, sagaId, actions.getUpdatedState().get(), actions.getUpdatedSagaData().get(), MessageBuilder
                        .withPayload("{}")
                        .withHeader(ReplyMessageHeaders.REPLY_OUTCOME, CommandReplyOutcome.FAILURE.name())
                        .withHeader(ReplyMessageHeaders.REPLY_TYPE, Failure.class.getName())
                        .build());

            } else {
                // only do this if successful

                String lastRequestId = sagaCommandProducer.sendCommands(this.getSagaType(), sagaId, actions.getCommands(), this.makeSagaReplyChannel());
                sagaInstance.setLastRequestId(lastRequestId);

                updateState(sagaInstance, actions);

                sagaInstance.setSerializedSagaData(SagaDataSerde.serializeSagaData(actions.getUpdatedSagaData().orElse(sagaData)));

                if (actions.isEndState()) {
                    performEndStateActions(sagaId, sagaInstance, actions.isCompensating(), actions.isFailed(), sagaData);
                }

                sagaInstanceRepository.update(sagaInstance);

                if (actions.isReplyExpected()) {
                    break;
                } else {
                    actions = simulateSuccessfulReplyToLocalActionOrNotification(sagaType, sagaId, actions);
                }

            }
        }
    }

    private SagaActions<Data> simulateSuccessfulReplyToLocalActionOrNotification(String sagaType, String sagaId, SagaActions<Data> actions) {
        return getStateDefinition().handleReply(sagaType, sagaId, actions.getUpdatedState().get(), actions.getUpdatedSagaData().get(), MessageBuilder
                .withPayload("{}")
                .withHeader(ReplyMessageHeaders.REPLY_OUTCOME, CommandReplyOutcome.SUCCESS.name())
                .withHeader(ReplyMessageHeaders.REPLY_TYPE, Success.class.getName())
                .build());
    }

    private void updateState(SagaInstance sagaInstance, SagaActions<Data> actions) {
        actions.getUpdatedState().ifPresent(stateName -> {
            sagaInstance.setStateName(stateName);
            sagaInstance.setEndState(actions.isEndState());
            sagaInstance.setCompensating(actions.isCompensating());
            sagaInstance.setFailed(actions.isFailed());
        });
    }

    private Boolean isReplyForThisSagaType(Message message) {
        return message.getHeader(SagaReplyHeaders.REPLY_SAGA_TYPE).map(x -> x.equals(getSagaType())).orElse(false);
    }
}
