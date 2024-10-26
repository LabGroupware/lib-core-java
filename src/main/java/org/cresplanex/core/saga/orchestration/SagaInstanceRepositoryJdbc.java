package org.cresplanex.core.saga.orchestration;

import java.util.HashSet;
import java.util.Set;

import org.cresplanex.core.common.id.IdGenerator;
import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SagaInstanceRepositoryJdbc implements SagaInstanceRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final IdGenerator idGenerator;

    private final SagaInstanceRepositorySql sagaInstanceRepositorySql;

    public SagaInstanceRepositoryJdbc(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            IdGenerator idGenerator,
            CoreSchema coreSchema) {
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        this.idGenerator = idGenerator;

        sagaInstanceRepositorySql = new SagaInstanceRepositorySql(coreSchema);
    }

    @Override
    public void save(SagaInstance sagaInstance) {
        sagaInstance.setId(idGenerator.genIdAsString());
        logger.info("Saving {} {}", sagaInstance.getSagaType(), sagaInstance.getId());

        coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getInsertIntoSagaInstanceSql(),
                sagaInstanceRepositorySql.makeSaveArgs(sagaInstance));

        saveDestinationsAndResources(sagaInstance);
    }

    private void saveDestinationsAndResources(SagaInstance sagaInstance) {
        for (DestinationAndResource dr : sagaInstance.getDestinationsAndResources()) {
            try {
                coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getInsertIntoSagaInstanceParticipantsSql(),
                        sagaInstance.getSagaType(),
                        sagaInstance.getId(),
                        dr.getDestination(),
                        dr.getResource()
                );
            } catch (CoreDuplicateKeyException e) {
                logger.info("key duplicate: sagaType = {}, sagaId = {}, destination = {}, resource = {}",
                        sagaInstance.getSagaType(),
                        sagaInstance.getId(),
                        dr.getDestination(),
                        dr.getResource());
            }
        }
    }

    @Override
    public SagaInstance find(String sagaType, String sagaId) {
        logger.info("finding {} {}", sagaType, sagaId);

        Set<DestinationAndResource> destinationsAndResources = new HashSet<>(coreJdbcStatementExecutor.query(
                sagaInstanceRepositorySql.getSelectFromSagaInstanceParticipantsSql(),
                (rs, rownum)
                -> new DestinationAndResource(rs.getString("destination"), rs.getString("resource")),
                sagaType,
                sagaId));

        return coreJdbcStatementExecutor.query(
                sagaInstanceRepositorySql.getSelectFromSagaInstanceSql(),
                (rs, rownum) -> sagaInstanceRepositorySql.mapToSagaInstance(sagaType, sagaId, destinationsAndResources, new JdbcSqlQueryRow(rs)),
                sagaType,
                sagaId).stream().findFirst().orElseThrow(() -> new RuntimeException(String.format("Cannot find saga instance %s %s", sagaType, sagaId)));
    }

    @Override
    public void update(SagaInstance sagaInstance) {
        logger.info("Updating {} {}", sagaInstance.getSagaType(), sagaInstance.getId());
        int count = coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getUpdateSagaInstanceSql(),
                sagaInstanceRepositorySql.makeUpdateArgs(sagaInstance));

        if (count != 1) {
            throw new RuntimeException("Should be 1 : " + count);
        }

        saveDestinationsAndResources(sagaInstance);
    }

}
