package org.cresplanex.core.messaging.producer.jdbc;

import org.cresplanex.core.common.id.IdGenerator;
import org.cresplanex.core.common.jdbc.CoreCommonJdbcOperations;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.producer.MessageProducerImplementation;

public class MessageProducerJdbcImplementation implements MessageProducerImplementation {

    private final CoreCommonJdbcOperations coreCommonJdbcOperations;
    private final IdGenerator idGenerator;

    private final CoreSchema coreSchema;

    public MessageProducerJdbcImplementation(CoreCommonJdbcOperations coreCommonJdbcOperations,
            IdGenerator idGenerator,
            CoreSchema coreSchema) {

        this.coreCommonJdbcOperations = coreCommonJdbcOperations;
        this.idGenerator = idGenerator;
        this.coreSchema = coreSchema;
    }

    @Override
    public void send(Message message) {
        String id = coreCommonJdbcOperations.insertIntoMessageTable(idGenerator,
                message.getPayload(),
                message.getRequiredHeader(Message.DESTINATION),
                message.getHeaders(),
                coreSchema);

        message.setHeader(Message.ID, id);
    }
}
