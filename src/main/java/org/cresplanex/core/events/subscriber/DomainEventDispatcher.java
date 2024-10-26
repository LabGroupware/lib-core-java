package org.cresplanex.core.events.subscriber;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.events.common.EventMessageHeaders;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

public class DomainEventDispatcher {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String eventDispatcherId;
  private final DomainEventHandlers domainEventHandlers;
  private final MessageConsumer messageConsumer;

  private final DomainEventNameMapping domainEventNameMapping;

  public DomainEventDispatcher(String eventDispatcherId, DomainEventHandlers domainEventHandlers, MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
    this.eventDispatcherId = eventDispatcherId;
    this.domainEventHandlers = domainEventHandlers;
    this.messageConsumer = messageConsumer;
    this.domainEventNameMapping = domainEventNameMapping;
  }

  public void initialize() {
    logger.info("Initializing domain event dispatcher");
    messageConsumer.subscribe(eventDispatcherId, domainEventHandlers.getAggregateTypesAndEvents(), this::messageHandler);
    logger.info("Initialized domain event dispatcher");
  }

  public void messageHandler(Message message) {
    String aggregateType = message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE);

    message.setHeader(EventMessageHeaders.EVENT_TYPE,
            domainEventNameMapping.externalEventTypeToEventClassName(aggregateType, message.getRequiredHeader(EventMessageHeaders.EVENT_TYPE)));

    Optional<DomainEventHandler> handler = domainEventHandlers.findTargetMethod(message);

    if (!handler.isPresent()) {
      return;
    }

    DomainEvent param = JSonMapper.fromJson(message.getPayload(), handler.get().getEventClass());

    handler.get().invoke(new DomainEventEnvelopeImpl<>(message,
            aggregateType,
            message.getRequiredHeader(EventMessageHeaders.AGGREGATE_ID),
            message.getRequiredHeader(Message.ID),
            param));

  }


}
