package org.cresplanex.core.saga.simpledsl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.messaging.common.Message;

/**
 * 参加者のアクションまたは補償処理を行うステップを表現するクラスです。
 *
 * @param <Data> サガデータのタイプ
 */
public class ParticipantInvocationStep<Data> implements SagaStep<Data> {

    private final Map<String, HandlerAndClass<Data>> actionReplyHandlers;
    private final Map<String, HandlerAndClass<Data>> compensationReplyHandlers;
    private final Optional<ParticipantInvocation<Data>> participantInvocation;
    private final Optional<ParticipantInvocation<Data>> compensation;

    /**
     * コンストラクタ。
     *
     * @param participantInvocation アクションの参加者インボケーション
     * @param compensation 補償処理の参加者インボケーション
     * @param actionReplyHandlers アクションの返信ハンドラー
     * @param compensationReplyHandlers 補償処理の返信ハンドラー
     */
    public ParticipantInvocationStep(
            Optional<ParticipantInvocation<Data>> participantInvocation,
            Optional<ParticipantInvocation<Data>> compensation,
            Map<String, HandlerAndClass<Data>> actionReplyHandlers,
            Map<String, HandlerAndClass<Data>> compensationReplyHandlers
    ) {
        this.actionReplyHandlers = actionReplyHandlers;
        this.compensationReplyHandlers = compensationReplyHandlers;
        this.participantInvocation = participantInvocation;
        this.compensation = compensation;
    }

    private Optional<ParticipantInvocation<Data>> getParticipantInvocation(boolean compensating) {
        return compensating ? compensation : participantInvocation;
    }

    @Override
    public boolean hasAction(Data data) {
        return participantInvocation.isPresent() && participantInvocation.map(p -> p.isInvocable(data)).orElse(true);
    }

    @Override
    public boolean hasCompensation(Data data) {
        return compensation.isPresent() && compensation.map(p -> p.isInvocable(data)).orElse(true);
    }

    @Override
    public Optional<HandlerAndClass<Data>> getReplyHandler(Message message, boolean compensating) {
        String replyType = message.getRequiredHeader(ReplyMessageHeaders.REPLY_TYPE);
        return Optional.ofNullable((compensating ? compensationReplyHandlers : actionReplyHandlers).get(replyType));
    }

    @Override
    public boolean isSuccessfulReply(boolean compensating, Message message) {
        return getParticipantInvocation(compensating).get().isSuccessfulReply(message);
    }

    @Override
    public StepOutcome makeStepOutcome(Data data, boolean compensating) {
        return StepOutcome.makeRemoteStepOutcome(getParticipantInvocation(compensating)
                .map(pi -> pi.makeCommandToSend(data))
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList));
    }

}
