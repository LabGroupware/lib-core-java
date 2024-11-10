package org.cresplanex.core.saga.orchestration;

import java.util.Optional;

import org.cresplanex.core.saga.orchestration.repository.SagaInstance;

/**
 * Saga管理のためのインターフェース。Sagaのインスタンスを作成、管理します。
 *
 * @param <Data> Sagaデータの型
 */
public interface SagaManager<Data> {

    /**
     * 新しいSagaインスタンスを作成します。
     *
     * @param sagaData 初期化するSagaデータ
     * @return 新しく作成されたSagaインスタンス
     */
    SagaInstance create(Data sagaData);

    /**
     * リプライチャンネルへの購読を開始します。
     */
    void subscribeToReplyChannel();

    /**
     * 指定したロックターゲットと共にSagaインスタンスを作成します。
     *
     * @param sagaData 初期化するSagaデータ
     * @param lockTarget ロックターゲットのオプショナル値
     * @return 新しく作成されたSagaインスタンス
     */
    SagaInstance create(Data sagaData, Optional<String> lockTarget);

    /**
     * ターゲットクラスとIDを使用して新しいSagaインスタンスを作成します。
     *
     * @param data 初期化するSagaデータ
     * @param targetType ロックターゲットのクラス
     * @param targetId ロックターゲットのID
     * @return 新しく作成されたSagaインスタンス
     */
    SagaInstance create(Data data, String targetType, String targetId);
}
