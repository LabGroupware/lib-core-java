/**
 * This package contains the classes that are responsible for orchestrating the
 * saga.
 *
 * サーガをオーケストレーションするクラスを提供するパッケージ.
 *
 * {@link DestinationAndResource}では送信先とリソースを表すクラスを提供.
 *
 * {@link org.cresplanex.core.saga.simpledsl.SimpleSagaDefinition}を用いない場合は,
 * {@link SagaDefinition}を実装してサーガの開始とメッセージ応答のハンドリングを定義する.
 *
 * {@link Saga}はSagaの定義であり, それごとに{@link SagaManager},
 * その実装である{@link SagaManagerImpl}が存在する.
 * 一つ一つの{@link SagaManager}の生成は{@link SagaManagerFactory}によって行われる.
 *
 * {@link SagaInstanceFactory}は{@link SagaManager}と{@link Saga}をマップ管理していて,
 * {@link Saga}とSagaDataを受け取ることで, 対応する{@link SagaManager}を取得し,
 * その{@link SagaManager}によって {@link #create(Data sagaData)}が実行される. また,
 * {@link SagaInstanceFactory}はコンストラクタでの{@link SagaManager}の生成後,
 * {@link #subscribeToReplyChannel()}を呼び出すことで, リプライチャンネルの購読を開始する.
 *
 * <h3>createの流れ</h3>
 * <ol>
 * <li>タイプとデータのみでサーガインスタンスを生成する</li>
 * <li>IDを付与してサーガインスタンス情報と参加者情報の保存を行う</li>
 * <li>サーガに定義された{@link #onStarting(String sagaId, Data data)}を実行する</li>
 * <li>ロックが必要な場合はロックを獲得する</li>
 * <li>サーガに定義された{@link SagaDefinition}のstartである{@link SagaActions}を取得する
 * <ul>
 * <li>{@link SagaActions}では, 送信するコマンド, 更新されたサーガデータ, 更新されたサガの状態名, サガが終了状態かどうか,
 * コンペンセーションかどうか, ローカルかどうか, ローカル例外, 失敗かどうかが含まれている.</li>
 * </ul>
 * </li>
 * <li>{@link #processActions(String sagaType, String sagaId, SagaInstance sagaInstance, Data sagaData, SagaActions<Data>
 * actions)}を実行する</li>
 * </ol>
 *
 * <h3>processActions</h3>
 * <ul>
 * <li>ループでアクションが実行されており, 2パターンにより処理が分かれる</li>
 * <ol>
 * <li>localExceptionがnullでない場合
 * <ul>
 * <li>ローカル例外がある場合は, 失敗を示すリプライアクションをセット</li>
 * </ul>
 * </li>
 * <li>localExceptionがnullの場合
 * <ul>
 * <li>{@link org.cresplanex.core.saga.orchestration.command.SagaCommandProducer}を使ってアクションに定義されたコマンド({@link CommandWithDestinationAndType})を送信する.
 * このときのリプライチャネルは内部で定義されている.</li>
 * <li>lastRequestIdには, 送信したメッセージIDを格納する</li>
 * <li>actionsの{@link #getUpdatedState}が空でなく, stateが入っている場合は,
 * サーガの状態を更新する(stateName, isEndState, isCompensating, isFailed)</li>
 * <li>{@link #setSerializedSagaData}でサーガデータのシリアライズしたものをインスタンスに登録</li>
 * <li>isEndStateがtrueの場合は,
 * {@link #performEndStateActions(String sagaId, SagaInstance sagaInstance, boolean compensating, boolean failed, Data sagaData)}を行う</li>
 * <li>sagaInstanceの永続化情報を更新する</li>
 * <li>{@link #isReplyExpected}がtrueの場合は, break, これにより,
 * サガは送信したメッセージに対する応答を待つ「待機」状態に入り, ループを続けることなく処理を一旦終了
 * リプライが期待されない場合はすぐに次のアクションを進める必要があるため,
 * {@link #simulateSuccessfulReplyToLocalActionOrNotification}でアクションを更新する</li>
 * </ul>
 * </li>
 * </ol>
 * </ul>
 *
 * <h3>performEndStateActions</h3>
 * <ul>
 * <li>全ての参加者に対して, unlockを送信し,
 * ロックを解放する.このときのリプライチャネルも{@link #processActions}で定義したものを利用する.</li>
 * <li>また, null サーガで定義してある{@link #onSagaCompletedSuccessfully(String sagaId, Data data)},
 * {@link #onSagaRolledBack(String sagaId, Data data)}, {@link #onSagaFailed(String sagaId, Data data)}を状況に応じて実行する</li>
 * </ul>
 *
 * <h3>simulateSuccessfulReplyToLocalActionOrNotification</h3>
 * <ul>
 * <li>成功したリプライをシミュレートするアクションを返す</li>
 * </ul>
 *
 * <h3>subscribeToReplyChannel</h3>
 * <ul>
 * <li>{@link #handleMessage}をハンドラにして, リプライチャンネルを購読する</li>
 * </ul>
 *
 * <h3>handleMessage</h3>
 * <ul>
 * <li>REPLY_SAGA_IDがある場合のみ{@link #handleReply(Message message)}を実行</li>
 * </ul>
 *
 * <h3>handleReply</h3>
 * <ul>
 * <li>このsagaタイプでない場合は無視する</li>
 * <li>sagaInstanceRepositoryを利用してsagaInstanceをDBから取得後, sagaDataをデシリアライズ</li>
 * <li>REPLY_LOCKEDがある場合はDESTINATIONとともに{@link DestinationAndResource}としてインスタンスに登録</li>
 * <li>sagaインスタンスの現在の状態やsagaデータ,
 * messageなどを基に{@link org.cresplanex.core.saga.orchestration.SagaDefinition#handleReply}を実行して次のアクションを取得</li>
 * <li>これをもとに{@link #processActions}を実行</li>
 * </ul>
 */
package org.cresplanex.core.saga.orchestration;
