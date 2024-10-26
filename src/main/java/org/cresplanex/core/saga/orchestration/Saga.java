package org.cresplanex.core.saga.orchestration;


public interface Saga<Data> {

  SagaDefinition<Data> getSagaDefinition();

  default String getSagaType() {
    return getClass().getName().replace("$", "_DLR_");
  }

  default void onStarting(String sagaId, Data data) {  }
  default void onSagaCompletedSuccessfully(String sagaId, Data data) {  }
  default void onSagaRolledBack(String sagaId, Data data) {  }
  default void onSagaFailed(String sagaId, Data data) {};
}
