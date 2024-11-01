package org.cresplanex.core.saga.participant;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.lock.SagaLockManager;

/**
 * SagaCommandDispatcherFactoryは、SagaCommandDispatcherのインスタンスを生成するファクトリクラスです。
 * 必要な依存オブジェクトを注入して、Dispatcherのセットアップを簡素化します。
 */
public class SagaCommandDispatcherFactory {

    private final MessageConsumer messageConsumer;
    private final SagaLockManager sagaLockManager;
    private final CommandNameMapping commandNameMapping;
    private final CommandReplyProducer commandReplyProducer;

    /**
     * SagaCommandDispatcherFactoryのコンストラクタ。
     *
     * @param messageConsumer メッセージコンシューマー
     * @param sagaLockManager サガのロック管理を行うマネージャー
     * @param commandNameMapping コマンド名のマッピング
     * @param commandReplyProducer コマンドの返信プロデューサー
     */
    public SagaCommandDispatcherFactory(MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            CommandNameMapping commandNameMapping,
            CommandReplyProducer commandReplyProducer) {
        this.messageConsumer = messageConsumer;
        this.sagaLockManager = sagaLockManager;
        this.commandNameMapping = commandNameMapping;
        this.commandReplyProducer = commandReplyProducer;
    }

    /**
     * 指定されたコマンドディスパッチャーIDとコマンドハンドラ群を基に、 新しいSagaCommandDispatcherインスタンスを生成します。
     *
     * @param commandDispatcherId コマンドディスパッチャーのID
     * @param target コマンドハンドラ群
     * @return 初期化されたSagaCommandDispatcherインスタンス
     */
    public SagaCommandDispatcher make(String commandDispatcherId, CommandHandlers target) {
        SagaCommandDispatcher sagaCommandDispatcher = new SagaCommandDispatcher(commandDispatcherId, target, messageConsumer, sagaLockManager, commandNameMapping, commandReplyProducer);
        sagaCommandDispatcher.initialize();
        return sagaCommandDispatcher;
    }
}
