package org.cresplanex.core.saga.participant;

import org.cresplanex.core.saga.common.*;
import org.cresplanex.core.saga.lock.LockTarget;
import org.cresplanex.core.saga.lock.SagaLockManager;
import org.cresplanex.core.saga.lock.SagaUnlockCommand;
import org.cresplanex.core.saga.lock.StashMessageRequiredException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.commands.consumer.CommandDispatcher;
import org.cresplanex.core.commands.consumer.CommandHandler;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.commands.consumer.CommandReplyToken;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

/**
 * SagaCommandDispatcherは, サガパターンのためのコマンドディスパッチャーです。 メッセージのロックやアンロック,
 * スタッシュ機能を使用してサガ内のコマンドメッセージを管理します。
 */
public class SagaCommandDispatcher extends CommandDispatcher {

    private final SagaLockManager sagaLockManager;

    /**
     * SagaCommandDispatcherのコンストラクタ。
     *
     * @param commandDispatcherId コマンドディスパッチャーのID
     * @param target ターゲットのコマンドハンドラ群
     * @param messageConsumer メッセージコンシューマー
     * @param sagaLockManager サガのロック管理を行うマネージャー
     * @param commandNameMapping コマンド名のマッピング
     * @param commandReplyProducer コマンドの返信プロデューサー
     */
    public SagaCommandDispatcher(String commandDispatcherId,
            CommandHandlers target,
            MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            CommandNameMapping commandNameMapping, CommandReplyProducer commandReplyProducer) {
        super(commandDispatcherId, target, messageConsumer, commandNameMapping, commandReplyProducer);
        this.sagaLockManager = sagaLockManager;
    }

    /**
     * 受信したメッセージを処理します。アンロックメッセージの場合, 対象をアンロックし, それ以外の
     * メッセージは通常の処理を行います。アンロック失敗時はメッセージをスタッシュします。
     *
     * @param message 処理するメッセージ
     */
    @Override
    public void messageHandler(Message message) {
        if (isUnlockMessage(message)) { // アンロックメッセージの場合
            String sagaId = getSagaId(message);
            String target = message.getRequiredHeader(CommandMessageHeaders.RESOURCE);
            // targetのロックを解放後, メッセージを処理
            sagaLockManager.unlock(sagaId, target).ifPresent(m -> super.messageHandler(message));
        } else {
            try {
                super.messageHandler(message);
            } catch (StashMessageRequiredException e) {
                // メッセージの処理中にStashMessageRequiredExceptionが発生した場合, メッセージをスタッシュ
                String sagaType = getSagaType(message);
                String sagaId = getSagaId(message);
                String target = e.getTarget();
                sagaLockManager.stashMessage(sagaType, sagaId, target, message);
            }
        }
    }

    /**
     * メッセージからサガIDを取得します。
     *
     * @param message メッセージ
     * @return サガID
     */
    private String getSagaId(Message message) {
        return message.getRequiredHeader(SagaCommandHeaders.SAGA_ID);
    }

    /**
     * メッセージからサガタイプを取得します。
     *
     * @param message メッセージ
     * @return サガタイプ
     */
    private String getSagaType(Message message) {
        return message.getRequiredHeader(SagaCommandHeaders.SAGA_TYPE);
    }

    /**
     * 指定されたコマンドハンドラを呼び出し, 必要に応じてロックを取得し, 返信メッセージにロックヘッダーを追加します。
     *
     * @param commandHandler コマンドハンドラ
     * @param cm コマンドメッセージ
     * @param pathVars パス変数
     * @param commandReplyToken コマンド返信トークン
     * @return 返信メッセージリスト
     */
    @Override
    protected List<Message> invoke(CommandHandler commandHandler, CommandMessage<?> cm, Map<String, String> pathVars, CommandReplyToken commandReplyToken) {
        Optional<String> lockedTarget = Optional.empty();

        if (commandHandler instanceof SagaCommandHandler sch) { // サガコマンドハンドラの場合
            if (sch.getPreLock().isPresent()) { // 事前ロック処理が設定されている場合
                // 事前ロック処理を実行
                LockTarget lockTarget = sch.getPreLock().get().apply(cm, new PathVariables(pathVars));
                Message message = cm.getMessage();
                String sagaType = getSagaType(message);
                String sagaId = getSagaId(message);
                String target = lockTarget.getTarget();
                lockedTarget = Optional.of(target);
                // ロックを取得できない場合, スタッシュ例外をスロー
                if (!sagaLockManager.claimLock(sagaType, sagaId, target)) {
                    throw new StashMessageRequiredException(target);
                }
            }
        }

        // 通常のメッセージハンドラを呼び出し
        List<Message> messages = super.invoke(commandHandler, cm, pathVars, commandReplyToken);

        if (commandHandler instanceof SagaCommandHandler sch) { // サガコマンドハンドラの場合
            if (sch.getPostLock().isPresent()) { // 事後ロック処理が設定されている場合
                for (Message message : messages) {
                    // 事後ロック処理を実行
                    LockTarget lockTarget = sch.getPostLock().get().apply(cm, new PathVariables(pathVars), message);
                    String sagaType = getSagaType(message);
                    String sagaId = getSagaId(message);
                    String target = lockTarget.getTarget();
                    lockedTarget = Optional.of(target);
                    // ロックを取得できない場合, スタッシュ例外をスロー
                    if (!sagaLockManager.claimLock(sagaType, sagaId, target)) {
                        throw new StashMessageRequiredException(target);
                    }
                }
            }
        }

        if (lockedTarget.isPresent()) { // ロック対象がある場合
            // ロックヘッダーを追加
            return addLockedHeader(messages, lockedTarget.get());
        } else { // ロック対象がない場合
            // メッセージからロック対象を取得
            Optional<LockTarget> lt = getLock(messages);
            if (lt.isPresent()) { // ロック対象がある場合
                Message message = cm.getMessage();
                String sagaType = getSagaType(message);
                String sagaId = getSagaId(message);

                // ロックを取得し, できない場合, 例外をスロー
                if (!sagaLockManager.claimLock(sagaType, sagaId, lt.get().getTarget())) {
                    // throw new RuntimeException("Cannot claim lock"); // TODO: なぜランタイムをスローする?
                    throw new StashMessageRequiredException(lt.get().getTarget());
                }

                // ロックヘッダーを追加
                return addLockedHeader(messages, lt.get().getTarget());
            } else {
                // ロック対象がない場合, メッセージリストをそのまま返す
                return messages;
            }
        }
    }

    /**
     * メッセージリストからロックターゲットを取得します。
     *
     * @param messages メッセージリスト
     * @return ロックターゲットのOptionalオブジェクト
     */
    private Optional<LockTarget> getLock(List<Message> messages) {
        return messages.stream().filter(m -> m instanceof SagaReplyMessage && ((SagaReplyMessage) m).hasLockTarget()).findFirst().flatMap(m -> ((SagaReplyMessage) m).getLockTarget());
    }

    /**
     * ロック対象のヘッダーをメッセージリストに追加します。
     *
     * @param messages メッセージリスト
     * @param lockedTarget ロック対象
     * @return ロックヘッダーが追加されたメッセージリスト
     */
    private List<Message> addLockedHeader(List<Message> messages, String lockedTarget) {
        return messages.stream().map(m -> MessageBuilder.withMessage(m).withHeader(SagaReplyHeaders.REPLY_LOCKED, lockedTarget).build()).collect(Collectors.toList());
    }

    /**
     * メッセージがアンロックメッセージかどうかを判定します。
     *
     * @param message メッセージ
     * @return アンロックメッセージの場合はtrue, それ以外はfalse
     */
    private boolean isUnlockMessage(Message message) {
        return message.getRequiredHeader(CommandMessageHeaders.COMMAND_TYPE).equals(SagaUnlockCommand.class.getName());
    }
}
