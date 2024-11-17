package org.cresplanex.core.saga.orchestration;

import static java.util.Collections.singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.Failure;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.Success;
import org.cresplanex.core.commands.producer.CommandProducer;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.common.SagaCommandHeaders;
import org.cresplanex.core.saga.common.SagaReplyHeaders;
import org.cresplanex.core.saga.lock.LockTarget;
import org.cresplanex.core.saga.lock.SagaLockManager;
import org.cresplanex.core.saga.lock.SagaUnlockCommand;
import org.cresplanex.core.saga.lock.TargetConflictOnSagaStartException;
import org.cresplanex.core.saga.orchestration.command.SagaCommandProducer;
import org.cresplanex.core.saga.orchestration.repository.SagaInstance;
import org.cresplanex.core.saga.orchestration.repository.SagaInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * サーガの実行管理を行う {@link SagaManagerImpl} クラス。
 * 指定されたサーガのライフサイクルを管理し、メッセージの受信とコマンドの送信を行います。
 */
public class SagaManagerImpl<Data>
        implements SagaManager<Data> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Saga<Data> saga;
    private SagaInstanceRepository sagaInstanceRepository;
    private CommandProducer commandProducer;
    private MessageConsumer messageConsumer;
    private SagaLockManager sagaLockManager;
    private SagaCommandProducer sagaCommandProducer;

    /**
     * コンストラクタ。依存するオブジェクトを注入し、サーガの実行を管理するための準備を行います。
     *
     * @param saga サーガの定義
     * @param sagaInstanceRepository サーガインスタンスのリポジトリ
     * @param commandProducer コマンドプロデューサー
     * @param messageConsumer メッセージコンシューマー
     * @param sagaLockManager サーガロックマネージャー
     * @param sagaCommandProducer サーガコマンドプロデューサー
     */
    public SagaManagerImpl(Saga<Data> saga,
            SagaInstanceRepository sagaInstanceRepository,
            CommandProducer commandProducer,
            MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            SagaCommandProducer sagaCommandProducer) {
        this.saga = saga;
        this.sagaInstanceRepository = sagaInstanceRepository;
        this.commandProducer = commandProducer;
        this.messageConsumer = messageConsumer;
        this.sagaLockManager = sagaLockManager;
        this.sagaCommandProducer = sagaCommandProducer;
    }

    public void setSagaCommandProducer(SagaCommandProducer sagaCommandProducer) {
        this.sagaCommandProducer = sagaCommandProducer;
    }

    public void setSagaInstanceRepository(SagaInstanceRepository sagaInstanceRepository) {
        this.sagaInstanceRepository = sagaInstanceRepository;
    }

    public void setCommandProducer(CommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    public void setMessageConsumer(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void setSagaLockManager(SagaLockManager sagaLockManager) {
        this.sagaLockManager = sagaLockManager;
    }

    @Override
    public SagaInstance create(Data sagaData) {
        return create(sagaData, Optional.empty());
    }

    @Override
    public SagaInstance create(Data data, String targetType, String targetId) {
        return create(data, Optional.of(new LockTarget(targetType, targetId).getTarget()));
    }

    /**
     * 新しいサーガインスタンスを作成し、必要に応じてロックを獲得します。 サーガの開始アクションを実行し、アクション結果を処理します。
     *
     * @param sagaData サーガに渡すデータ
     * @param resource オプションのロック対象リソース
     * @return 作成されたサーガインスタンス
     */
    @Override
    public SagaInstance create(Data sagaData, Optional<String> resource) {

        // SagaInstanceの作成
        // ただし, ここではサーガのタイプとデータのみを保存する
        SagaInstance sagaInstance = new SagaInstance(getSagaType(),
                null,
                "????",
                null,
                SagaDataSerde.serializeSagaData(sagaData), new HashSet<>());

        // サーガインスタンス上方と参加者情報を保存
        sagaInstanceRepository.save(sagaInstance);

        // 生成されたIDを取得
        String sagaId = sagaInstance.getId();

        // sagaスタート時のコールバックを実行
        saga.onStarting(sagaId, sagaData);

        // ロックが必要な場合はロックを獲得
        // この時スタッシュをしない理由としては,
        // 1. まだSagaが開始されていないため, 補償処理が必要ない
        // 2. 例えば, あるTargetに対するUpdateSaga処理中に, そのTargetに対する別のUpdateSagaが来た場合,
        // 反映されてない状況での判断による誤った操作である可能性が高いため.
        resource.ifPresent(r -> {
            // 例えば, IDがデータベース生成であった場合に, 事前に作成をしておき,
            // そこからSagaを開始させたい場合などのユースケース.
            // 他の場合は, withLockを使って, ロックする
            if (!sagaLockManager.claimLock(getSagaType(), sagaId, r)) {
                throw new TargetConflictOnSagaStartException("Failed to claim lock for resource: " + r);
            }
            // 最後に解放を行うため
            sagaInstance.addDestinationsAndResources(singleton(
                    new DestinationAndResource(getSagaCommandSelfChannel(), r)));
        });

        // サーガの開始アクションを取得
        SagaActions<Data> actions = getStateDefinition().start(sagaData);
        // 初めのStepが失敗した場合は, 即時終了
        // SagaInstanceFactoryのcreateメソッド内で, このメソッドが呼ばれるため,
        // ここでの例外はSagaInstanceFactoryのcreateメソッドに伝播する
        actions.getLocalException().ifPresent(e -> {
            performEndStateActions(sagaId, sagaInstance, false, true, sagaData);
            throw e;
        });

        processActions(saga.getSagaType(), sagaId, sagaInstance, sagaData, actions);

        return sagaInstance;
    }

    /**
     * サーガの終了アクションを実行し、サーガの完了、ロールバック、失敗時の処理を行います。
     *
     * @param sagaId サーガID
     * @param sagaInstance サーガインスタンス
     * @param compensating ロールバック中かどうか
     * @param failed 失敗したかどうか
     * @param sagaData サーガデータ
     */
    private void performEndStateActions(String sagaId, SagaInstance sagaInstance, boolean compensating, boolean failed, Data sagaData) {
        for (DestinationAndResource dr : sagaInstance.getDestinationsAndResources()) { // 全てのアンロックが必要なDestinationAndResourceに対して
            Map<String, String> headers = new HashMap<>();
            // saga情報をヘッダに追加
            headers.put(SagaCommandHeaders.SAGA_ID, sagaId);
            headers.put(SagaCommandHeaders.SAGA_TYPE, getSagaType());
            commandProducer.send(dr.getDestination(), dr.getResource(), new SagaUnlockCommand(), SagaUnlockCommand.TYPE, makeSagaReplyChannel(), headers);
        }

        if (failed) {
            saga.onSagaFailed(sagaId, sagaData);
        }
        if (compensating) {
            saga.onSagaRolledBack(sagaId, sagaData);
        } else {
            saga.onSagaCompletedSuccessfully(sagaId, sagaData);
        }

    }

    private SagaDefinition<Data> getStateDefinition() {
        SagaDefinition<Data> sm = saga.getSagaDefinition();

        if (sm == null) {
            throw new RuntimeException("state machine cannot be null");
        }

        return sm;
    }

    private String getSagaType() {
        return saga.getSagaType();
    }

    private String getSagaCommandSelfChannel() {
        return saga.getSagaCommandSelfChannel();
    }

    /**
     * サーガのリプライチャンネルにサブスクライブします。
     */
    @Override
    public void subscribeToReplyChannel() {
        messageConsumer.subscribe(saga.getSagaType() + "-consumer", singleton(makeSagaReplyChannel()),
                this::handleMessage);
    }

    private String makeSagaReplyChannel() {
        return getSagaType() + "-reply";
    }

    /**
     * メッセージを処理します。リプライメッセージの場合はリプライ処理を行います。
     *
     * @param message 処理するメッセージ
     */
    public void handleMessage(Message message) {
        logger.info("handle message invoked {}", message);
        if (message.hasHeader(SagaReplyHeaders.REPLY_SAGA_ID)) {
            handleReply(message);
        } else {
            logger.warn("Handle message doesn't know what to do with: {} ", message);
        }
    }

    /**
     * リプライメッセージを処理し、サーガの現在の状態とアクションを実行します。
     *
     * @param message 処理するリプライメッセージ
     */
    private void handleReply(Message message) {

        // リプライがこのサーガタイプに対するものであるかどうかを確認
        if (!isReplyForThisSagaType(message)) {
            return;
        }

        logger.debug("Handle reply: {}", message);

        String sagaId = message.getRequiredHeader(SagaReplyHeaders.REPLY_SAGA_ID);
        String sagaType = message.getRequiredHeader(SagaReplyHeaders.REPLY_SAGA_TYPE);

        SagaInstance sagaInstance = sagaInstanceRepository.find(sagaType, sagaId);
        Data sagaData = SagaDataSerde.deserializeSagaData(sagaInstance.getSerializedSagaData());

        message.getHeader(SagaReplyHeaders.REPLY_LOCKED).ifPresent(lockedTarget -> {
            // 最後にアンロックするためにDestinationAndResourceを追加
            String destination = message.getRequiredHeader(CommandMessageHeaders.inReply(CommandMessageHeaders.DESTINATION));
            sagaInstance.addDestinationsAndResources(singleton(new DestinationAndResource(destination, lockedTarget)));
        });

        String currentState = sagaInstance.getStateName();

        SagaActions<Data> actions = getStateDefinition().handleReply(sagaType, sagaId, currentState, sagaData, message);

        processActions(sagaType, sagaId, sagaInstance, sagaData, actions);
    }

    /**
     * 指定されたアクションに基づいてサーガの状態を更新し、必要に応じてリプライを待ちます。
     *
     * @param sagaType サーガタイプ
     * @param sagaId サーガID
     * @param sagaInstance サーガインスタンス
     * @param sagaData サーガデータ
     * @param actions 実行するアクション
     */
    private void processActions(String sagaType, String sagaId, SagaInstance sagaInstance, Data sagaData, SagaActions<Data> actions) {

        while (true) {

            if (actions.getLocalException().isPresent()) {

                // ローカル例外がある場合は, 失敗のリプライが送られる代わりにダミーでこのリプライメッセージを登録したアクションを次のアクションとする.
                // ただし, 失敗メッセージなので、補償トランザクションの開始が行われるようにisSuccessfulReplyなどを実装する必要がある。
                // 失敗メッセージがきたことを仮定して処理
                // 投げられた例外が何かは関係ない
                actions = getStateDefinition().handleReply(sagaType, sagaId, actions.getUpdatedState().get(), actions.getUpdatedSagaData().get(), MessageBuilder
                        .withPayload("{}")
                        .withHeader(ReplyMessageHeaders.REPLY_OUTCOME, CommandReplyOutcome.FAILURE.name())
                        .withHeader(ReplyMessageHeaders.REPLY_TYPE, Failure.TYPE)
                        .build());

            } else {
                // lastRequestIdには, 送信したメッセージIDが入る
                // localStepの場合は, commandsが空なので, 何もせずlastRequestIdをnullはなる.
                String lastRequestId = sagaCommandProducer.sendCommands(this.getSagaType(), sagaId, actions.getCommands(), this.makeSagaReplyChannel());
                sagaInstance.setLastRequestId(lastRequestId);

                // アクションに定義されたstateが入っている場合は, サーガの状態を更新する
                updateState(sagaInstance, actions);

                // サーガデータのシリアライズしたものをインスタンスに登録
                sagaInstance.setSerializedSagaData(SagaDataSerde.serializeSagaData(actions.getUpdatedSagaData().orElse(sagaData)));

                // サーガが終了状態の場合は, サーガの終了処理を行う
                if (actions.isEndState()) {
                    performEndStateActions(sagaId, sagaInstance, actions.isCompensating(), actions.isFailed(), sagaData);
                }

                logger.info("Updating saga instance: {}", sagaInstance);

                // サーガインスタンス情報を更新
                sagaInstanceRepository.update(sagaInstance);

                if (actions.isReplyExpected()) {
                    // リプライを期待する場合 = 先程のsendCommandsで送信したコマンドに対するリプライを待つ必要がある場合,
                    // 一度breakすることで, subscribeToReplyChannelで登録したハンドラが呼ばれるのを待つ
                    // これをトリガーにして, stateが動き出す
                    break;
                } else {
                    // リプライが期待されない場合、すぐに次のアクションが実行できるので
                    actions = simulateSuccessfulReplyToLocalActionOrNotification(sagaType, sagaId, actions);
                }
            }
        }
    }

    /**
     * 成功応答をシミュレートします。 メッセージにはダミーでの成功応答が含まれており, 次のアクションが実行されます。
     * localStepの場合はもとより, onReplyの登録はできず, notificationの場合も必要としないため, このメソッドが呼ばれる。
     * ただし, あくまで成功メッセージなので、補償トランザクションの開始は行われないようにisSuccessfulReplyなどを実装する必要がある。
     *
     * @param sagaType サーガタイプ
     * @param sagaId サーガID
     * @param actions 実行するアクション
     * @return 次のアクション
     */
    private SagaActions<Data> simulateSuccessfulReplyToLocalActionOrNotification(String sagaType, String sagaId, SagaActions<Data> actions) {
        return getStateDefinition().handleReply(sagaType, sagaId, actions.getUpdatedState().get(), actions.getUpdatedSagaData().get(), MessageBuilder
                .withPayload("{}")
                .withHeader(ReplyMessageHeaders.REPLY_OUTCOME, CommandReplyOutcome.SUCCESS.name())
                .withHeader(ReplyMessageHeaders.REPLY_TYPE, Success.TYPE)
                .build());
    }

    private void updateState(SagaInstance sagaInstance, SagaActions<Data> actions) {
        actions.getUpdatedState().ifPresent(stateName -> {
            sagaInstance.setStateName(stateName);
            sagaInstance.setEndState(actions.isEndState());
            sagaInstance.setCompensating(actions.isCompensating());
            sagaInstance.setFailed(actions.isFailed());
        });
    }

    private Boolean isReplyForThisSagaType(Message message) {
        return message.getHeader(SagaReplyHeaders.REPLY_SAGA_TYPE).map(x -> x.equals(getSagaType())).orElse(false);
    }
}
