package org.cresplanex.core.saga.lock;

public class TargetConflictOnSagaStartException extends RuntimeException {

    public TargetConflictOnSagaStartException(String message) {
        super(message);
    }
}
