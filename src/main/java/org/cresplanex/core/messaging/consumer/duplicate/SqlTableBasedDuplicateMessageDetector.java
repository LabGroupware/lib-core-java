package org.cresplanex.core.messaging.consumer.duplicate;

import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.jdbc.CoreTransactionTemplate;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTableBasedDuplicateMessageDetector implements DuplicateMessageDetector {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreSchema coreSchema;
    private final String currentTimeInMillisecondsSql;
    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final CoreTransactionTemplate coreTransactionTemplate;

    public SqlTableBasedDuplicateMessageDetector(CoreSchema coreSchema,
            String currentTimeInMillisecondsSql,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreTransactionTemplate coreTransactionTemplate) {
        this.coreSchema = coreSchema;
        this.currentTimeInMillisecondsSql = currentTimeInMillisecondsSql;
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        this.coreTransactionTemplate = coreTransactionTemplate;
    }

    @Override
    public boolean isDuplicate(String consumerId, String messageId) {
        try {
            String table = coreSchema.qualifyTable("received_messages");

            coreJdbcStatementExecutor.update(String.format("insert into %s(consumer_id, message_id, creation_time) values(?, ?, %s)",
                    table,
                    currentTimeInMillisecondsSql),
                    consumerId,
                    messageId);

            return false;
        } catch (CoreDuplicateKeyException e) {
            logger.info("Message duplicate: consumerId = {}, messageId = {}", consumerId, messageId);
            return true;
        }
    }

    @Override
    public void doWithMessage(SubscriberIdAndMessage subscriberIdAndMessage, Runnable callback) {
        coreTransactionTemplate.executeInTransaction(() -> {
            if (!isDuplicate(subscriberIdAndMessage.getSubscriberId(), subscriberIdAndMessage.getMessage().getId())) {
                callback.run();
            }
            return null;
        });
    }
}
