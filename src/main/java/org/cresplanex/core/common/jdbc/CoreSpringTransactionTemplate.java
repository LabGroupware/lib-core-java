package org.cresplanex.core.common.jdbc;

import java.util.function.Supplier;

import org.springframework.transaction.support.TransactionTemplate;

public class CoreSpringTransactionTemplate implements CoreTransactionTemplate {

    private final TransactionTemplate transactionTemplate;

    public CoreSpringTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public <T> T executeInTransaction(Supplier<T> callback) {
        return transactionTemplate.execute(status -> callback.get());
    }
}
