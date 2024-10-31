package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

/**
 * CommandDispatcherFactory は、コマンドディスパッチャーを生成するファクトリクラスです。
 * このクラスは、指定されたメッセージコンシューマー、コマンド名マッピング、および
 * コマンドリプライプロデューサーを使用して、コマンドディスパッチャーを作成します。
 */
public class CommandDispatcherFactory {

    private final MessageConsumer messageConsumer;
    private final CommandNameMapping commandNameMapping;
    private final CommandReplyProducer commandReplyProducer;

    /**
     * CommandDispatcherFactory のコンストラクタ。
     *
     * @param messageConsumer メッセージの消費者
     * @param commandNameMapping コマンド名のマッピング
     * @param commandReplyProducer コマンドリプライプロデューサー
     */
    public CommandDispatcherFactory(MessageConsumer messageConsumer,
            CommandNameMapping commandNameMapping, CommandReplyProducer commandReplyProducer) {
        this.messageConsumer = messageConsumer;
        this.commandNameMapping = commandNameMapping;
        this.commandReplyProducer = commandReplyProducer;
    }

    /**
     * コマンドディスパッチャーを生成します。 指定されたコマンドディスパッチャーIDおよびコマンドハンドラを使用して新しい
     * CommandDispatcher オブジェクトを作成し、初期化します。
     *
     * @param commandDispatcherId コマンドディスパッチャーのID
     * @param commandHandlers コマンドハンドラのリスト
     * @return 初期化済みの CommandDispatcher オブジェクト
     */
    public CommandDispatcher make(String commandDispatcherId,
            CommandHandlers commandHandlers) {
        CommandDispatcher commandDispatcher = new CommandDispatcher(commandDispatcherId, commandHandlers, messageConsumer, commandNameMapping, commandReplyProducer);
        commandDispatcher.initialize();
        return commandDispatcher;
    }
}
