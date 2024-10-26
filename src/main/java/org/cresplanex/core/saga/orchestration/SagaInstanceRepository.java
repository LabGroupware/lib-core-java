package org.cresplanex.core.saga.orchestration;

public interface SagaInstanceRepository {

    void save(SagaInstance sagaInstance);

    SagaInstance find(String sagaType, String sagaId);

    void update(SagaInstance sagaInstance);

}
