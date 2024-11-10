package org.cresplanex.core.commands.consumer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.cresplanex.core.messaging.common.Message;

/**
 * コマンドハンドラのリストを管理し、対象チャネルや例外ハンドラの取得を行います。
 */
public class CommandHandlers {

    private final List<CommandHandler> handlers;

    /**
     * コマンドハンドラのリストを使用して新しい CommandHandlers インスタンスを初期化します。
     *
     * @param handlers コマンドハンドラのリスト
     */
    public CommandHandlers(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * コマンドハンドラが対応するすべてのチャネルを取得します。
     *
     * @return チャネルのセット
     */
    public Set<String> getChannels() {
        return handlers.stream().map(CommandHandler::getChannel).collect(toSet());
    }

    /**
     * メッセージに対して適切なコマンドハンドラを見つけ出します。
     *
     * @param message 対象メッセージ
     * @return 見つかった場合は対応するコマンドハンドラ、見つからない場合は Optional.empty()
     */
    public Optional<CommandHandler> findTargetMethod(Message message) {
        return handlers.stream().filter(h -> h.handles(message)).findFirst();
    }

    /**
     * 指定されたコマンドハンドラと原因例外に基づき、例外ハンドラを取得します。 未サポートの操作例外をスローします。
     *
     * @param commandHandler 例外を発生させたコマンドハンドラ
     * @param cause 発生した例外
     * @return 見つかった場合は例外ハンドラ、見つからない場合は Optional.empty()
     * @throws UnsupportedOperationException
     * 指定されたコマンドハンドラとチャネルに対する例外処理がサポートされていない場合
     */
    public Optional<CommandExceptionHandler> findExceptionHandler(CommandHandler commandHandler, Throwable cause) {
        throw new UnsupportedOperationException(String.format("A command handler for command of type %s on channel %s threw an exception",
                commandHandler.getCommandType(),
                commandHandler.getChannel()),
                cause);
    }

    /**
     * すべてのコマンドハンドラのリストを返します。
     *
     * @return コマンドハンドラのリスト
     */
    public List<CommandHandler> getHandlers() {
        return handlers;
    }
}
