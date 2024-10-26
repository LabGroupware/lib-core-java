package org.cresplanex.core.saga.orchestration;

public interface SagaStateMachineAction<Data, Reply> {

    SagaActions<Data> apply(Data data, Reply reply);

}
