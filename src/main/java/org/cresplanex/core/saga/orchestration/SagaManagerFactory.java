package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.commands.producer.CommandProducer;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.lock.SagaLockManager;
import org.cresplanex.core.saga.orchestration.command.SagaCommandProducer;
import org.cresplanex.core.saga.orchestration.repository.SagaInstanceRepository;

/**
 * サーガマネージャーのファクトリクラス。 各サーガに対応する {@link SagaManagerImpl} のインスタンスを作成します。
 */
public class SagaManagerFactory {

    private final SagaInstanceRepository sagaInstanceRepository;
    private final CommandProducer commandProducer;
    private final MessageConsumer messageConsumer;
    private final SagaLockManager sagaLockManager;
    private final SagaCommandProducer sagaCommandProducer;

    /**
     * コンストラクタ。必要な依存関係を注入します。
     *
     * @param sagaInstanceRepository サーガインスタンスのリポジトリ
     * @param commandProducer コマンドのプロデューサー
     * @param messageConsumer メッセージのコンシューマー
     * @param sagaLockManager サーガロックの管理マネージャー
     * @param sagaCommandProducer サーガコマンドのプロデューサー
     */
    public SagaManagerFactory(SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager, SagaCommandProducer sagaCommandProducer) {
        this.sagaInstanceRepository = sagaInstanceRepository;
        this.commandProducer = commandProducer;
        this.messageConsumer = messageConsumer;
        this.sagaLockManager = sagaLockManager;
        this.sagaCommandProducer = sagaCommandProducer;
    }

    /**
     * 指定されたサーガのために {@link SagaManagerImpl} を作成します。
     *
     * @param <SagaData> サーガデータの型
     * @param saga 作成するサーガマネージャーの対象となるサーガ
     * @return 新しく作成された {@link SagaManagerImpl} インスタンス
     */
    public <SagaData> SagaManagerImpl<SagaData> make(Saga<SagaData> saga) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }
}
