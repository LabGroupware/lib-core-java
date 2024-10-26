package org.cresplanex.core.saga.common;

import org.cresplanex.core.common.jdbc.CoreSchema;

public class SagaLockManagerSql {

    private final String insertIntoSagaLockTableSql;
    private final String insertIntoSagaStashTableSql;
    private final String selectFromSagaLockTableSql;
    private final String selectFromSagaStashTableSql;
    private final String updateSagaLockTableSql;
    private final String deleteFromSagaLockTableSql;
    private final String deleteFromSagaStashTableSql;

    public SagaLockManagerSql(CoreSchema coreSchema) {

        String sagaLockTable = coreSchema.qualifyTable("saga_lock_table");
        String sagaStashTable = coreSchema.qualifyTable("saga_stash_table");

        insertIntoSagaLockTableSql = String.format("INSERT INTO %s(target, saga_type, saga_id) VALUES(?, ?,?)", sagaLockTable);
        insertIntoSagaStashTableSql = String.format("INSERT INTO %s(message_id, target, saga_type, saga_id, message_headers, message_payload) VALUES(?, ?, ?, ?, ?, ?)", sagaStashTable);
        selectFromSagaLockTableSql = String.format("select saga_id from %s WHERE target = ? FOR UPDATE", sagaLockTable);
        selectFromSagaStashTableSql = String.format("select message_id, target, saga_type, saga_id, message_headers, message_payload from %s WHERE target = ? ORDER BY message_id LIMIT 1", sagaStashTable);
        updateSagaLockTableSql = String.format("update %s set saga_type = ?, saga_id = ? where target = ?", sagaLockTable);
        deleteFromSagaLockTableSql = String.format("delete from %s where target = ?", sagaLockTable);
        deleteFromSagaStashTableSql = String.format("delete from %s where message_id = ?", sagaStashTable);
    }

    public String getInsertIntoSagaLockTableSql() {
        return insertIntoSagaLockTableSql;
    }

    public String getInsertIntoSagaStashTableSql() {
        return insertIntoSagaStashTableSql;
    }

    public String getSelectFromSagaLockTableSql() {
        return selectFromSagaLockTableSql;
    }

    public String getSelectFromSagaStashTableSql() {
        return selectFromSagaStashTableSql;
    }

    public String getUpdateSagaLockTableSql() {
        return updateSagaLockTableSql;
    }

    public String getDeleteFromSagaLockTableSql() {
        return deleteFromSagaLockTableSql;
    }

    public String getDeleteFromSagaStashTableSql() {
        return deleteFromSagaStashTableSql;
    }
}
