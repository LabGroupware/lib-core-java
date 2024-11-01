package org.cresplanex.core.saga.orchestration;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cresplanex.core.saga.orchestration.repository.SagaInstance;

/**
 * サーガインスタンスを生成するためのファクトリクラス。 各サーガに対応する {@link SagaManager} を作成および管理します。
 */
public class SagaInstanceFactory {

    // 各サーガに対応するサーガマネージャーのマップ
    private final ConcurrentMap<Saga<?>, SagaManager<?>> sagaManagers = new ConcurrentHashMap<>();

    /**
     * サーガインスタンスファクトリを初期化し、各サーガに対してサーガマネージャーを生成します。
     *
     * @param sagaManagerFactory サーガマネージャーを作成するファクトリ
     * @param sagas 管理するサーガのコレクション
     */
    public SagaInstanceFactory(SagaManagerFactory sagaManagerFactory, Collection<Saga<?>> sagas) {
        for (Saga<?> saga : sagas) {
            sagaManagers.put(saga, makeSagaManager(sagaManagerFactory, saga));
        }
    }

    /**
     * 指定されたサーガデータでサーガインスタンスを生成します。
     *
     * @param <SagaData> サーガデータの型
     * @param saga サーガインスタンスを生成する対象のサーガ
     * @param data サーガデータ
     * @return 生成されたサーガインスタンス
     * @throws RuntimeException 対応するサーガマネージャーが存在しない場合にスローされます
     */
    @SuppressWarnings("unchecked")
    public <SagaData> SagaInstance create(Saga<SagaData> saga, SagaData data) {
        SagaManager<SagaData> sagaManager = (SagaManager<SagaData>) sagaManagers.get(saga);

        if (sagaManager == null) {
            throw new RuntimeException("No SagaManager for " + saga);
        }
        return sagaManager.create(data);
    }

    /**
     * 指定されたサーガのためにサーガマネージャーを生成し、リプライチャンネルを購読します。
     *
     * @param <SagaData> サーガデータの型
     * @param sagaManagerFactory サーガマネージャーを生成するファクトリ
     * @param saga サーガマネージャーを生成する対象のサーガ
     * @return 作成されたサーガマネージャー
     */
    private <SagaData> SagaManager<SagaData> makeSagaManager(SagaManagerFactory sagaManagerFactory, Saga<SagaData> saga) {
        SagaManagerImpl<SagaData> sagaManager = sagaManagerFactory.make(saga);
        sagaManager.subscribeToReplyChannel();
        return sagaManager;
    }
}
