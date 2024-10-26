package org.cresplanex.core.saga.common;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.producer.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SagaLockManagerImpl implements SagaLockManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;

    private final SagaLockManagerSql sagaLockManagerSql;

    public SagaLockManagerImpl(CoreJdbcStatementExecutor coreJdbcStatementExecutor, CoreSchema coreSchema) {
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;

        sagaLockManagerSql = new SagaLockManagerSql(coreSchema);
    }

    @Override
    public boolean claimLock(String sagaType, String sagaId, String target) {
        while (true)
      try {
            coreJdbcStatementExecutor.update(sagaLockManagerSql.getInsertIntoSagaLockTableSql(), target, sagaType, sagaId);
            logger.debug("Saga {} {} has locked {}", sagaType, sagaId, target);
            return true;
        } catch (CoreDuplicateKeyException e) {
            Optional<String> owningSagaId = selectForUpdate(target);
            if (owningSagaId.isPresent()) {
                if (owningSagaId.get().equals(sagaId)) {
                    return true;
                } else {
                    logger.debug("Saga {} {} is blocked by {} which has locked {}", sagaType, sagaId, owningSagaId, target);
                    return false;
                }
            }
            logger.debug("{}  is repeating attempt to lock {}", sagaId, target);
        }
    }

    private Optional<String> selectForUpdate(String target) {
        return coreJdbcStatementExecutor
                .query(sagaLockManagerSql.getSelectFromSagaLockTableSql(), (rs, rowNum) -> rs.getString("saga_id"), target).stream().findFirst();
    }

    @Override
    public void stashMessage(String sagaType, String sagaId, String target, Message message) {

        logger.debug("Stashing message from {} for {} : {}", sagaId, target, message);

        coreJdbcStatementExecutor.update(sagaLockManagerSql.getInsertIntoSagaStashTableSql(),
                message.getRequiredHeader(Message.ID),
                target,
                sagaType,
                sagaId,
                JSonMapper.toJson(message.getHeaders()),
                message.getPayload());
    }

    @Override
    public Optional<Message> unlock(String sagaId, String target) {
        Optional<String> owningSagaId = selectForUpdate(target);

        if (!owningSagaId.isPresent()) {
            throw new RuntimeException("owningSagaId is not present");
        }

        if (!owningSagaId.get().equals(sagaId)) {
            throw new RuntimeException(String.format("Expected owner to be %s but is %s", sagaId, owningSagaId.get()));
        }

        logger.debug("Saga {} has unlocked {}", sagaId, target);

        @SuppressWarnings("unchecked")
        List<StashedMessage> stashedMessages = coreJdbcStatementExecutor.query(sagaLockManagerSql.getSelectFromSagaStashTableSql(), (rs, rowNum) -> {
            return new StashedMessage(
                    rs.getString("saga_type"),
                    rs.getString("saga_id"),
                    MessageBuilder.withPayload(rs.getString("message_payload"))
                            .withExtraHeaders(
                                    "",
                                    JSonMapper.fromJson(rs.getString("message_headers"), Map.class)
                            )
                            .build()
            );
        }, target);

        if (stashedMessages.isEmpty()) {
            assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getDeleteFromSagaLockTableSql(), target));
            return Optional.empty();
        }

        StashedMessage stashedMessage = stashedMessages.get(0);

        logger.debug("unstashed from {}  for {} : {}", sagaId, target, stashedMessage.getMessage());

        assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getUpdateSagaLockTableSql(), stashedMessage.getSagaType(),
                stashedMessage.getSagaId(), target));
        assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getDeleteFromSagaStashTableSql(), stashedMessage.getMessage().getId()));

        return Optional.of(stashedMessage.getMessage());
    }

    private void assertEqualToOne(int n) {
        if (n != 1) {
            throw new RuntimeException("Expected to update one row but updated: " + n);
        }
    }
}
