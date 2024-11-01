package org.cresplanex.core.saga.orchestration.repository;

import java.util.HashSet;
import java.util.Set;

import org.cresplanex.core.common.id.IdGenerator;
import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.saga.orchestration.DestinationAndResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBCを使用してサーガインスタンスを永続化および操作するリポジトリクラス。
 */
public class SagaInstanceRepositoryJdbc implements SagaInstanceRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final IdGenerator idGenerator;
    private final SagaInstanceRepositorySql sagaInstanceRepositorySql;

    /**
     * コンストラクタ。JDBC実行やID生成、SQL文の構成に必要な要素を受け取ります。
     *
     * @param coreJdbcStatementExecutor JDBCステートメントを実行するためのエグゼキュータ
     * @param idGenerator IDを生成するためのジェネレータ
     * @param coreSchema 使用するデータベーススキーマ
     */
    public SagaInstanceRepositoryJdbc(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            IdGenerator idGenerator,
            CoreSchema coreSchema) {
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        this.idGenerator = idGenerator;
        this.sagaInstanceRepositorySql = new SagaInstanceRepositorySql(coreSchema);
    }

    /**
     * 新しいサーガインスタンスを保存します。
     *
     * @param sagaInstance 保存するサーガインスタンス
     */
    @Override
    public void save(SagaInstance sagaInstance) {
        // IDGeneratorによるID生成
        sagaInstance.setId(idGenerator.genIdAsString());
        logger.info("Saving {} {}", sagaInstance.getSagaType(), sagaInstance.getId());

        // SagaInstanceを保存
        coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getInsertIntoSagaInstanceSql(),
                sagaInstanceRepositorySql.makeSaveArgs(sagaInstance));

        // 宛先とリソースを保存
        saveDestinationsAndResources(sagaInstance);
    }

    /**
     * サーガインスタンスに関連する宛先とリソースを保存します。
     *
     * @param sagaInstance 宛先とリソースを含むサーガインスタンス
     */
    private void saveDestinationsAndResources(SagaInstance sagaInstance) {
        for (DestinationAndResource dr : sagaInstance.getDestinationsAndResources()) {
            try {
                // saga instanceでの全ての宛先とリソースを保存
                coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getInsertIntoSagaInstanceParticipantsSql(),
                        sagaInstance.getSagaType(),
                        sagaInstance.getId(),
                        dr.getDestination(),
                        dr.getResource()
                );
            } catch (CoreDuplicateKeyException e) {
                // 重複の場合は何もしない
                logger.info("key duplicate: sagaType = {}, sagaId = {}, destination = {}, resource = {}",
                        sagaInstance.getSagaType(),
                        sagaInstance.getId(),
                        dr.getDestination(),
                        dr.getResource());
            }
        }
    }

    /**
     * 指定されたサーガタイプとサーガIDに基づいてサーガインスタンスを検索します。
     * ここにはサーガの情報と参加者の情報が含まれます。
     *
     * @param sagaType サーガの種類
     * @param sagaId サーガID
     * @return 見つかったサーガインスタンス
     * @throws RuntimeException サーガインスタンスが見つからない場合
     */
    @Override
    public SagaInstance find(String sagaType, String sagaId) {
        logger.info("finding {} {}", sagaType, sagaId);

        // サーガインスタンス参加者の情報取得
        Set<DestinationAndResource> destinationsAndResources = new HashSet<>(coreJdbcStatementExecutor.query(
                sagaInstanceRepositorySql.getSelectFromSagaInstanceParticipantsSql(),
                (rs, rownum) -> new DestinationAndResource(rs.getString("destination"), rs.getString("resource")),
                sagaType,
                sagaId));

        // サーガインスタンスの情報取得
        return coreJdbcStatementExecutor.query(
                sagaInstanceRepositorySql.getSelectFromSagaInstanceSql(),
                (rs, rownum) -> sagaInstanceRepositorySql.mapToSagaInstance(sagaType, sagaId, destinationsAndResources, new JdbcSqlQueryRow(rs)),
                sagaType,
                sagaId).stream().findFirst().orElseThrow(() -> new RuntimeException(String.format("Cannot find saga instance %s %s", sagaType, sagaId)));
    }

    /**
     * 指定されたサーガインスタンスを更新します。
     *
     * @param sagaInstance 更新するサーガインスタンス
     * @throws RuntimeException 更新対象のサーガインスタンスが1件でない場合
     */
    @Override
    public void update(SagaInstance sagaInstance) {
        logger.info("Updating {} {}", sagaInstance.getSagaType(), sagaInstance.getId());
        // サーガインスタンスを更新
        int count = coreJdbcStatementExecutor.update(sagaInstanceRepositorySql.getUpdateSagaInstanceSql(),
                sagaInstanceRepositorySql.makeUpdateArgs(sagaInstance));

        if (count != 1) {
            throw new RuntimeException("Should be 1 : " + count);
        }

        // 宛先とリソースを保存
        // 重複の場合は何もしない
        saveDestinationsAndResources(sagaInstance);
    }
}
