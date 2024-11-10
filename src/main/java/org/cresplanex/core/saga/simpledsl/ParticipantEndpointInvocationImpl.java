package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.command.CommandWithDestinationAndType;

/**
 * 参加者エンドポイントのインボケーションを実行するクラスの実装。
 *
 * @param <Data> サガデータのタイプ
 * @param <C> コマンドのタイプ
 */
public class ParticipantEndpointInvocationImpl<Data, C extends Command> extends AbstractParticipantInvocation<Data> {

    private final CommandEndpoint<C> commandEndpoint;
    private final Function<Data, C> commandProvider;

    /**
     * コンストラクタ。
     *
     * @param invocablePredicate このインボケーションが実行可能かどうかを判定する条件
     * @param commandEndpoint コマンドエンドポイント
     * @param commandProvider コマンドを提供する関数
     */
    public ParticipantEndpointInvocationImpl(Optional<Predicate<Data>> invocablePredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        super(invocablePredicate);
        this.commandEndpoint = commandEndpoint;
        this.commandProvider = commandProvider;
    }

    /**
     * メッセージが成功応答であるかを判定します。
     *
     * @param message 受信メッセージ
     * @return メッセージが成功応答の場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean isSuccessfulReply(Message message) {
        return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
    }

    /**
     * 送信するコマンドを生成します。
     *
     * @param data サガデータ
     * @return CommandWithDestinationAndType 送信コマンド
     */
    @Override
    public CommandWithDestinationAndType makeCommandToSend(Data data) {
        return CommandWithDestinationAndType.command(commandEndpoint.getCommandChannel(), null, commandProvider.apply(data), commandEndpoint.getCommandType());
    }
}
