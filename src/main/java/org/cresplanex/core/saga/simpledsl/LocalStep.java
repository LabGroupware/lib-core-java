package org.cresplanex.core.saga.simpledsl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.messaging.common.Message;
import static org.cresplanex.core.saga.simpledsl.StepOutcome.makeLocalOutcome;

/**
 * サガにおけるローカルステップを表すクラス。 ローカルなビジネスロジックを実行し、補償処理や例外処理も管理します。
 *
 * @param <Data> サガのデータの型
 */
public class LocalStep<Data> implements SagaStep<Data> {

    private final Consumer<Data> localFunction;
    private final Optional<Consumer<Data>> compensation;
    private final List<LocalExceptionSaver<Data>> localExceptionSavers;
    private final List<Class<RuntimeException>> rollbackExceptions;

    /**
     * コンストラクタ
     *
     * @param localFunction ローカル処理として実行する関数
     * @param compensation 補償処理（オプション）
     * @param localExceptionSavers 例外の保存を行うLocalExceptionSaverのリスト
     * @param rollbackExceptions ロールバックを引き起こす例外クラスのリスト
     */
    public LocalStep(Consumer<Data> localFunction, Optional<Consumer<Data>> compensation,
            List<LocalExceptionSaver<Data>> localExceptionSavers, List<Class<RuntimeException>> rollbackExceptions) {
        this.localFunction = localFunction;
        this.compensation = compensation;
        this.localExceptionSavers = localExceptionSavers;
        this.rollbackExceptions = rollbackExceptions;
    }

    /**
     * このステップにアクションが含まれるかを返します。 ローカルステップには必ずアクションがあると見なします。
     *
     * @param data サガデータ
     * @return 常にtrueを返します
     */
    @Override
    public boolean hasAction(Data data) {
        return true;
    }

    /**
     * 補償処理が存在するかを確認します。
     *
     * @param data サガデータ
     * @return 補償処理が存在する場合はtrue
     */
    @Override
    public boolean hasCompensation(Data data) {
        return compensation.isPresent();
    }

    /**
     * 応答メッセージが成功であるかを判定します。
     *
     * @param compensating 補償中であるかどうか
     * @param message メッセージ
     * @return 成功した応答であればtrue
     */
    @Override
    public boolean isSuccessfulReply(boolean compensating, Message message) {
        return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
    }

    /**
     * メッセージの応答ハンドラを取得します。ローカルステップには応答ハンドラがありません。
     *
     * @param message メッセージ
     * @param compensating 補償中かどうか
     * @return 空のOptionalを返します
     */
    @Override
    public Optional<BiConsumer<Data, Object>> getReplyHandler(Message message, boolean compensating) {
        return Optional.empty();
    }

    /**
     * ステップの結果を生成します。エラーが発生した場合、ロールバックが必要か判定します。
     *
     * @param data サガデータ
     * @param compensating 補償中かどうか
     * @return ステップの結果（StepOutcome）
     */
    @Override
    public StepOutcome makeStepOutcome(Data data, boolean compensating) {
        try {
            if (compensating) {
                compensation.ifPresent(localStep -> localStep.accept(data));
            } else {
                localFunction.accept(data);
            }
            return makeLocalOutcome(Optional.empty());
        } catch (RuntimeException e) {
            localExceptionSavers.stream()
                    .filter(saver -> saver.shouldSave(e))
                    .findFirst()
                    .ifPresent(saver -> saver.save(data, e));
            if (rollbackExceptions.isEmpty() || rollbackExceptions.stream().anyMatch(c -> c.isInstance(e))) {
                return makeLocalOutcome(Optional.of(e)); 
            }else {
                throw e;
            }
        }
    }

}
