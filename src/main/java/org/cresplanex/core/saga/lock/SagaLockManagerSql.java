package org.cresplanex.core.saga.lock;

import org.cresplanex.core.common.jdbc.CoreSchema;

/**
 * SagaLockManager 用の SQL クエリを保持するクラス。 テーブル名は指定された CoreSchema に基づいて生成されます。
 */
public class SagaLockManagerSql {

    private final String insertIntoSagaLockTableSql;
    private final String insertIntoSagaStashTableSql;
    private final String selectFromSagaLockTableSql;
    private final String selectFromSagaStashTableSql;
    private final String updateSagaLockTableSql;
    private final String deleteFromSagaLockTableSql;
    private final String deleteFromSagaStashTableSql;

    /**
     * コンストラクタ。指定されたスキーマを用いて SQL クエリを初期化します。
     *
     * @param coreSchema データベーススキーマ
     */
    public SagaLockManagerSql(CoreSchema coreSchema) {

        String sagaLockTable = coreSchema.qualifyTable("saga_lock_table");
        String sagaStashTable = coreSchema.qualifyTable("saga_stash_table");

        // Lock Tableに対してレコードを挿入するSQL(target, saga_type, saga_id)
        insertIntoSagaLockTableSql = String.format("INSERT INTO %s(target, saga_type, saga_id) VALUES(?, ?, ?)", sagaLockTable);
        // Stash Tableに対してレコードを挿入するSQL(message_id, target, saga_type, saga_id, message_headers, message_payload)
        insertIntoSagaStashTableSql = String.format("INSERT INTO %s(message_id, target, saga_type, saga_id, message_headers, message_payload) VALUES(?, ?, ?, ?, ?, ?)", sagaStashTable);
        // Lock Tableからtargetを基にsaga_idを取得するSQL, ただし行レベルでロックを取得するためにFOR UPDATEを付与
        selectFromSagaLockTableSql = String.format("SELECT saga_id FROM %s WHERE target = ? FOR UPDATE", sagaLockTable);
        // Stash Tableからtargetを基に最も古いメッセージを取得するSQL
        selectFromSagaStashTableSql = String.format("SELECT message_id, target, saga_type, saga_id, message_headers, message_payload FROM %s WHERE target = ? ORDER BY message_id LIMIT 1", sagaStashTable);
        // Lock Tableのtargetを基にsaga_typeとsaga_idを更新するSQL
        updateSagaLockTableSql = String.format("UPDATE %s SET saga_type = ?, saga_id = ? WHERE target = ?", sagaLockTable);
        // Lock Tableからtargetを基にレコードを削除するSQL
        deleteFromSagaLockTableSql = String.format("DELETE FROM %s WHERE target = ?", sagaLockTable);
        // Stash Tableからmessage_idを基にレコードを削除するSQL
        deleteFromSagaStashTableSql = String.format("DELETE FROM %s WHERE message_id = ?", sagaStashTable);
    }

    /**
     * saga_lock_table にデータを挿入する SQL を取得します。
     *
     * @return 挿入用の SQL クエリ
     */
    public String getInsertIntoSagaLockTableSql() {
        return insertIntoSagaLockTableSql;
    }

    /**
     * saga_stash_table にデータを挿入する SQL を取得します。
     *
     * @return 挿入用の SQL クエリ
     */
    public String getInsertIntoSagaStashTableSql() {
        return insertIntoSagaStashTableSql;
    }

    /**
     * saga_lock_table からデータを取得する SQL を取得します。
     *
     * @return データ取得用の SQL クエリ
     */
    public String getSelectFromSagaLockTableSql() {
        return selectFromSagaLockTableSql;
    }

    /**
     * saga_stash_table からスタッシュメッセージを取得する SQL を取得します。
     *
     * @return データ取得用の SQL クエリ
     */
    public String getSelectFromSagaStashTableSql() {
        return selectFromSagaStashTableSql;
    }

    /**
     * saga_lock_table のデータを更新する SQL を取得します。
     *
     * @return 更新用の SQL クエリ
     */
    public String getUpdateSagaLockTableSql() {
        return updateSagaLockTableSql;
    }

    /**
     * saga_lock_table からデータを削除する SQL を取得します。
     *
     * @return 削除用の SQL クエリ
     */
    public String getDeleteFromSagaLockTableSql() {
        return deleteFromSagaLockTableSql;
    }

    /**
     * saga_stash_table からスタッシュメッセージを削除する SQL を取得します。
     *
     * @return 削除用の SQL クエリ
     */
    public String getDeleteFromSagaStashTableSql() {
        return deleteFromSagaStashTableSql;
    }
}
