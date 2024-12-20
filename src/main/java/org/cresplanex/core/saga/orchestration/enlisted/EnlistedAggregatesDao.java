package org.cresplanex.core.saga.orchestration.enlisted;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * エンリストされた集約の永続化を管理するDAOクラス。 集約の保存、検索を行います。
 */
public class EnlistedAggregatesDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;

    /**
     * CoreJdbcStatementExecutorを使用してDAOを初期化します。
     *
     * @param coreJdbcStatementExecutor JDBCステートメントエグゼキュータ
     */
    public EnlistedAggregatesDao(CoreJdbcStatementExecutor coreJdbcStatementExecutor) {
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
    }

    /**
     * 指定されたサガIDとエンリストされた集約のセットを保存します。 既に存在する場合、重複エラーを無視して保存しません。
     *
     * @param sagaId サガID
     * @param enlistedAggregates 保存するエンリストされた集約のセット
     */
    public void save(String sagaId, Set<EnlistedAggregate> enlistedAggregates) {
        for (EnlistedAggregate ela : enlistedAggregates) {
           boolean exists = !coreJdbcStatementExecutor.query("Select saga_id from saga_enlisted_aggregates where saga_id = ? AND aggregate_type = ? AND aggregate_id = ?",
                    (rs, rowNum) -> rs.getString("saga_id"),
                    sagaId,
                    ela.getAggregateClass().getName(),
                    ela.getAggregateId()).isEmpty();
            if (!exists) {
                coreJdbcStatementExecutor.update("INSERT INTO saga_enlisted_aggregates(saga_id, aggregate_type, aggregate_id) values(?,?,?)",
                        sagaId,
                        ela.getAggregateClass().getName(),
                        ela.getAggregateId());
            }
        }
    }

//    public void save(String sagaId, Set<EnlistedAggregate> enlistedAggregates) {
//        for (EnlistedAggregate ela : enlistedAggregates) {
//            try {
//                coreJdbcStatementExecutor.update("INSERT INTO saga_enlisted_aggregates(saga_id, aggregate_type, aggregate_id) values(?,?,?)",
//                        sagaId,
//                        ela.getAggregateClass(),
//                        ela.getAggregateId());
//            } catch (CoreDuplicateKeyException e) {
//                logger.info("Cannot save aggregate, key duplicate: sagaId = {}, aggregateClass = {}, aggregateId = {}",
//                        sagaId, ela.getAggregateClass(), ela.getAggregateId());
//                // 重複エラーを無視
//            }
//        }
//    }

    /**
     * 指定されたサガIDに関連するエンリストされた集約のセットを検索します。
     *
     * @param sagaId サガID
     * @return サガIDに関連するエンリストされた集約のセット
     */
    @SuppressWarnings("unchecked")
    public Set<EnlistedAggregate> findEnlistedAggregates(String sagaId) {
        return new HashSet<>(coreJdbcStatementExecutor.query("Select aggregate_type, aggregate_id from saga_enlisted_aggregates where saga_id = ?",
                (rs, rowNum) -> {
                    try {
                        return new EnlistedAggregate((Class<Object>) ClassUtils.getClass(rs.getString("aggregate_type")), rs.getString("aggregate_id"));
                    } catch (ClassNotFoundException e) {
                        logger.error("Class not found", e);
                        throw new RuntimeException("Class not found", e);
                    }
                },
                sagaId));
    }

    /**
     * 指定された集約タイプと集約IDに関連するサガIDのセットを検索します。
     *
     * @param aggregateType 集約タイプのクラス
     * @param aggregateId 集約ID
     * @return 集約に関連するサガIDのセット
     */
    public Set<String> findSagas(Class<?> aggregateType, String aggregateId) {
        return new HashSet<>(coreJdbcStatementExecutor.query("Select saga_id from saga_enlisted_aggregates where aggregate_type = ? AND  aggregate_id = ?",
                (rs, rowNum) -> rs.getString("aggregate_type"),
                aggregateType, aggregateId));
    }
}
