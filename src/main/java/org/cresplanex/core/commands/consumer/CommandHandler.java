package org.cresplanex.core.commands.consumer;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.messaging.common.Message;

/**
 * コマンドを処理し、メッセージのリストを生成するためのハンドラ。
 * AbstractCommandHandlerを継承し、指定されたコマンドクラスに応じて処理を行います。
 */
public class CommandHandler extends AbstractCommandHandler<List<Message>> {

    /**
     * 指定されたチャネル、リソース、コマンドクラスおよび処理関数を使用して CommandHandler のインスタンスを作成します。
     *
     * @param <C> コマンドの型
     * @param channel 処理対象のチャネル
     * @param resource 処理対象のリソース (存在する場合)
     * @param commandClass 処理するコマンドのクラス
     * @param handler コマンドハンドラ引数をリスト形式のメッセージに変換する関数
     */
    public <C extends Command> CommandHandler(String channel, Optional<String> resource,
            Class<C> commandClass,
            Function<CommandHandlerArgs<C>, List<Message>> handler) {

        super(channel, resource, commandClass, handler);
    }
}
