package org.cresplanex.core.saga.orchestration.repository;

import java.util.Set;

import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.saga.orchestration.DestinationAndResource;
import org.cresplanex.core.saga.orchestration.SerializedSagaData;

/**
 * サーガインスタンスのデータベース操作に必要なSQL文を生成するクラス。
 */
public class SagaInstanceRepositorySql {

    private final String insertIntoSagaInstanceSql;
    private final String insertIntoSagaInstanceParticipantsSql;
    private final String selectFromSagaInstanceSql;
    private final String selectFromSagaInstanceParticipantsSql;
    private final String updateSagaInstanceSql;

    /**
     * 指定されたスキーマを使用して、必要なSQL文を準備します。
     *
     * @param coreSchema 使用するデータベーススキーマ
     */
    public SagaInstanceRepositorySql(CoreSchema coreSchema) {
        String sagaInstanceTable = coreSchema.qualifyTable("saga_instance");
        String sagaInstanceParticipantsTable = coreSchema.qualifyTable("saga_instance_participants");

        // saga instanceテーブルへのレコード挿入用SQL文
        insertIntoSagaInstanceSql = String.format("INSERT INTO %s(saga_type, saga_id, state_name, last_request_id, saga_data_type, saga_data_json, end_state, compensating, failed) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", sagaInstanceTable);
        // saga instance participantsテーブルへのレコード挿入用SQL文
        insertIntoSagaInstanceParticipantsSql = String.format("INSERT INTO %s(saga_type, saga_id, destination, resource) values(?,?,?,?)", sagaInstanceParticipantsTable);

        // saga instanceテーブルからsaga_typeとsaga_idをもとにレコードを取得するためのSQL文
        selectFromSagaInstanceSql = String.format("SELECT * FROM %s WHERE saga_type = ? AND saga_id = ?", sagaInstanceTable);
        // saga instance participantsテーブルからsaga_typeとsaga_idをもとにDESTINATIONとRESOURCEを取得するためのSQL文
        selectFromSagaInstanceParticipantsSql = String.format("SELECT destination, resource FROM %s WHERE saga_type = ? AND saga_id = ?", sagaInstanceParticipantsTable);

        // saga instanceテーブルのレコードをsaga_typeとsaga_idをもとに更新するためのSQL文
        updateSagaInstanceSql = String.format("UPDATE %s SET state_name = ?, last_request_id = ?, saga_data_type = ?, saga_data_json = ?, end_state = ?, compensating = ?, failed = ? where saga_type = ? AND saga_id = ?", sagaInstanceTable);
    }

    /**
     * サーガインスタンスの挿入SQL文を取得します。
     *
     * @return サーガインスタンス挿入用SQL文
     */
    public String getInsertIntoSagaInstanceSql() {
        return insertIntoSagaInstanceSql;
    }

    /**
     * サーガインスタンス参加者の挿入SQL文を取得します。
     *
     * @return サーガインスタンス参加者挿入用SQL文
     */
    public String getInsertIntoSagaInstanceParticipantsSql() {
        return insertIntoSagaInstanceParticipantsSql;
    }

    /**
     * サーガインスタンスの取得SQL文を取得します。
     *
     * @return サーガインスタンス取得用SQL文
     */
    public String getSelectFromSagaInstanceSql() {
        return selectFromSagaInstanceSql;
    }

    /**
     * サーガインスタンス参加者の取得SQL文を取得します。
     *
     * @return サーガインスタンス参加者取得用SQL文
     */
    public String getSelectFromSagaInstanceParticipantsSql() {
        return selectFromSagaInstanceParticipantsSql;
    }

    /**
     * サーガインスタンスの更新SQL文を取得します。
     *
     * @return サーガインスタンス更新用SQL文
     */
    public String getUpdateSagaInstanceSql() {
        return updateSagaInstanceSql;
    }

    /**
     * サーガインスタンスを保存するための引数を準備します。
     *
     * @param sagaInstance 保存対象のサーガインスタンス
     * @return SQL実行用の引数配列
     */
    public Object[] makeSaveArgs(SagaInstance sagaInstance) {
        return new Object[]{sagaInstance.getSagaType(),
            sagaInstance.getId(),
            sagaInstance.getStateName(),
            sagaInstance.getLastRequestId(),
            sagaInstance.getSerializedSagaData().getSagaDataType(),
            sagaInstance.getSerializedSagaData().getSagaDataJSON(),
            sagaInstance.isEndState(),
            sagaInstance.isCompensating(),
            sagaInstance.isFailed()};
    }

    /**
     * サーガインスタンスを更新するための引数を準備します。
     *
     * @param sagaInstance 更新対象のサーガインスタンス
     * @return SQL実行用の引数配列
     */
    public Object[] makeUpdateArgs(SagaInstance sagaInstance) {
        return new Object[]{sagaInstance.getStateName(),
            sagaInstance.getLastRequestId(),
            sagaInstance.getSerializedSagaData().getSagaDataType(),
            sagaInstance.getSerializedSagaData().getSagaDataJSON(),
            sagaInstance.isEndState(), sagaInstance.isCompensating(), sagaInstance.isFailed(),
            sagaInstance.getSagaType(), sagaInstance.getId()};
    }

    /**
     * クエリ結果をサーガインスタンスにマッピングします。
     *
     * @param sagaType サーガの種類
     * @param sagaId サーガID
     * @param destinationsAndResources 関連する宛先とリソース
     * @param rs SQLクエリの結果行
     * @return マッピングされたサーガインスタンス
     */
    public SagaInstance mapToSagaInstance(String sagaType, String sagaId, Set<DestinationAndResource> destinationsAndResources, SqlQueryRow rs) {
        return new SagaInstance(sagaType, sagaId, rs.getString("state_name"),
                rs.getString("last_request_id"),
                new SerializedSagaData(rs.getString("saga_data_type"), rs.getString("saga_data_json")),
                destinationsAndResources,
                rs.getBoolean("end_state"),
                rs.getBoolean("compensating"),
                rs.getBoolean("failed")
        );
    }
}
