package org.cresplanex.core.saga.orchestration.repository;

/**
 * サガインスタンスの永続化に関するリポジトリインターフェース。
 */
public interface SagaInstanceRepository {

    /**
     * サガインスタンスを保存します。
     *
     * @param sagaInstance 保存するサガインスタンス
     */
    void save(SagaInstance sagaInstance);

    /**
     * 指定されたサガの種類とIDに基づいてサガインスタンスを検索します。
     *
     * @param sagaType サガの種類
     * @param sagaId サガID
     * @return 見つかったサガインスタンス
     */
    SagaInstance find(String sagaType, String sagaId);

    /**
     * サガインスタンスを更新します。
     *
     * @param sagaInstance 更新するサガインスタンス
     */
    void update(SagaInstance sagaInstance);
}
