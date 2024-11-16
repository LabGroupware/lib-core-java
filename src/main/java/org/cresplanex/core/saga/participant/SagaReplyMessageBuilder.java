package org.cresplanex.core.saga.participant;

import java.util.Optional;

import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.Success;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.cresplanex.core.saga.lock.LockTarget;

/**
 * SagaReplyMessageBuilderは、サガ応答メッセージを構築するためのビルダークラスです。 応答にロック情報を含めることができます。
 */
public class SagaReplyMessageBuilder extends MessageBuilder {

    private Optional<LockTarget> lockTarget = Optional.empty();

    /**
     * コンストラクタ
     *
     * @param lockTarget ロック対象
     */
    public SagaReplyMessageBuilder(LockTarget lockTarget) {
        this.lockTarget = Optional.of(lockTarget);
    }

    /**
     * 指定されたタイプとIDを使用して、ロック情報を持つSagaReplyMessageBuilderを作成します。
     *
     * @param type ロック対象のタイプ
     * @param id ロック対象のID
     * @return 新しいSagaReplyMessageBuilderインスタンス
     */
    public static SagaReplyMessageBuilder withLock(String type, String id) {
        return new SagaReplyMessageBuilder(new LockTarget(type, id));
    }

    /**
     * 指定された応答オブジェクトと結果を使用してメッセージを構築します。
     *
     * @param <T> 応答オブジェクトの型
     * @param reply 応答オブジェクト
     * @param outcome コマンド応答の結果
     * @return 構築されたメッセージ
     */
    private <T> Message with(T reply, CommandReplyOutcome outcome) {
        return with(reply, reply.getClass().getName(), outcome);
    }

    /**
     * 指定された応答オブジェクト、応答タイプ、結果を使用してメッセージを構築します。
     *
     * @param reply 応答オブジェクト
     * @param replyType 応答タイプ
     * @param outcome コマンド応答の結果
     * @return 構築されたメッセージ
     * @param <T> 応答オブジェクトの型
     */
    private <T> Message with(T reply, String replyType, CommandReplyOutcome outcome) {
        this.body = JSonMapper.toJson(reply);
        withHeader(ReplyMessageHeaders.REPLY_OUTCOME, outcome.name());
        withHeader(ReplyMessageHeaders.REPLY_TYPE, replyType);
        return new SagaReplyMessage(body, headers, throwException, lockTarget);
    }

    /**
     * 成功した応答を持つメッセージを構築します。
     *
     * @param reply 成功応答オブジェクト
     * @return 成功応答メッセージ
     */
    public Message withSuccess(Object reply) {

        return with(reply, CommandReplyOutcome.SUCCESS);
    }

    public Message withSuccess(Object reply, String replyType) {
        return with(reply, replyType, CommandReplyOutcome.SUCCESS);
    }

    /**
     * 成功を示すデフォルト応答メッセージを構築します。
     *
     * @return デフォルトの成功応答メッセージ
     */
    public Message withSuccess() {
        return withSuccess(new Success(), Success.TYPE);
    }

}
