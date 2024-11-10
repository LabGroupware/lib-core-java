/**
 * org.cresplanex.core.saga.simpledsl パッケージ
 *
 * このパッケージは、シンプルなDSL（ドメイン固有言語）を用いたサガ（Saga）パターンの実装を提供します。
 * このDSLは、Sagaの各ステップを管理し、ローカルおよびリモートのコマンド、通知、コンペンセーション（補償）処理を
 * シンプルかつ柔軟に定義できるようにするためのフレームワークです。以下に、このパッケージ内の各クラスおよびインターフェースの役割と、
 * それぞれの依存関係について説明します。
 *
 * 以下が外部パッケージから利用される主要なクラスとインターフェースです。
 * <ul>
 * <li>{@link CommandEndpointBuilder}: コマンドエンドポイントを構築するためのビルダークラスです。</li>
 * <li>{@link SimpleSaga}: ユーザーはこれを実装した、サーガをbean登録することになる.</li>
 * </ul>
 *
 * <p>
 * <b>コマンドエンドポイント:</b>
 * </p>
 *
 * - {@link CommandEndpoint}:
 * サガ内でのコマンドのエンドポイントを定義するクラスで、コマンドチャネル、コマンドクラス、および対応する応答クラスのセットを含みます。
 *
 * - {@link CommandEndpointBuilder}: コマンドエンドポイントのビルダーで、チャネルと返信クラスを含む設定を行います。
 *
 * <p>
 * <b>アクションプロバイダ:</b>
 * </p>
 *
 * - {@link AbstractSagaActionsProvider}:
 * サガステップの結果を基に{@link SagaActions}を生成するための抽象クラスです。{@link SagaActionsProvider}がそのラッパーとなる.
 * 行うことは, 自身の保持するアクションとsupplierを利用して, コールバック関数を渡してtoSagaActionsを呼ぶことで,
 * {@link SagaActions}を生成します。 ただし,
 * 実際の今回の使用では単に登録したアクションをそのまま返す恒等関数がコールバック関数に利用される 使用方法となっているため,
 * インスタンス化時に登録したアクションがそのまま返されます。
 *
 * <p>
 * <b>参加者呼び出し:</b>
 * </p>
 *
 * - {@link ParticipantInvocation}:
 * サガ内での参加者のアクションやコマンドを定義するためのインターフェースです。具体的には、応答が成功であるかの判定、呼び出し可能かどうかの判定、
 * 送信するコマンドの生成を行います。
 * {@link AbstractParticipantInvocation}が{@link ParticipantInvocation}の抽象クラスで、呼び出し可能かどうかを判定するための条件を保持します。
 * 呼び出しが可能かどうかを判定する条件を与えることで、呼び出しできるかどうかを判断します。
 * 具体的な実装は{@link ParticipantInvocationImpl}や
 * {@link ParticipantEndpointInvocationImpl}が行い, 応答の成功判定は同じ処理であるが,
 * コマンドの生成方法が異なります。
 * {@link org.cresplanex.core.saga.simpledsl.ParticipantInvocationImpl}は、実行条件の他にコマンドを作成する関数と通知の有無を保持します。
 * {@link org.cresplanex.core.saga.simpledsl.ParticipantEndpointInvocationImpl}は、参加者エンドポイントのインボケーションを実行するクラスの実装で,
 * 実行条件の他にコマンドエンドポイントとコマンドプロバイダを保持します。直接{@link CommandWithDestination}
 * のビルダを指定する{@link ParticipantInvocationImpl}とは異なり,
 * コマンドエンドポイントと{@link Command}のみのビルダ(Provider)を指定します。
 *
 *
 * <p>
 * <li>Saga実行ステート:</li>
 * </p>
 *
 * - {@link SagaExecutionState}:
 * サガの実行状態を管理するクラスで、現在の実行ステップや補償モードの有無、エンドステートへの到達, 失敗の有無を追跡します。
 * 現在のステップを数字で管理しており, {@link SagaExecutionState#nextState(int size)}では,
 * 補償であるかどうかを判定し, 補償であれば, 現在のステップをデクリメント, そうでなければインクリメントします。
 *
 * -
 * {@link SagaExecutionStateJsonSerde}: {@link SagaExecutionState}をJSON形式にエンコードおよびデコードするユーティリティクラスです。
 *
 * <p>
 * <b>アウトカム:</b>
 * </p>
 *
 * - {@link StepOutcome}: サガステップの結果を表す抽象クラスです。
 * {@link #visit(Consumer<Optional<RuntimeException>> localConsumer,
 * Consumer<List<CommandWithDestinationAndType>> commandsConsumer)}が実行されたときに,
 * 適切なコンシューマーを利用して, outcomeを処理します。
 * サブクラスには、{@link StepOutcome.LocalStepOutcome}と{@link StepOutcome.RemoteStepOutcome}があり,
 * {@link StepOutcome.LocalStepOutcome}は{@link Optional<RuntimeException>}を保持し,
 * {@link StepOutcome.RemoteStepOutcome}は{@link List<CommandWithDestinationAndType>}を保持します。
 * 結果によってアクションにlocalやlocalException, commandsなどを登録する際に利用されます。
 *
 * <p>
 * <b>1ステップの実行:</b>
 * </p>
 *
 * - {@link AbstractStepToExecute}: サガのステップを実行するための抽象クラスです。
 * ステップやスキップしたステップの数、補償処理中かどうかを保持します。 {@link #size()}では, スキップしたステップ数+1を返し, null {@link #makeSagaActions(SagaActions.Builder, Object, SagaExecutionState, boolean)}
 * {@link SagaActions.Builder}や{@link org.cresplanex.core.saga.simpledsl.SagaExecutionStateJsonSerde.encodeState}を利用して,
 * Data data, SagaExecutionState newState, boolean
 * compensatingに基づいた{@link SagaActions}を生成します。
 *
 * - {@link StepToExecute}: 特定のサガステップを実行するためのクラスで,
 * {@link AbstractStepToExecute}を継承しています。
 * {@link #executeStep(Object, SagaExecutionState)}では, 現在のサガの実行状態に基づいて,
 * 新しいサガアクションを生成します。 size()分だけステートを進め, {@link SagaActions.Builder}を初期化.
 * {@link SagaStep#makeStepOutcome(Object, boolean)}を呼び出し,
 * 結果({@link StepOutcome})に基づいて,local, localException, commandsなどのアクションが登録.
 * このbuilderを利用して,
 * {@link AbstractStepToExecute#makeSagaActions(SagaActions.Builder, Object, SagaExecutionState, boolean)}
 * を実行することで, {@link SagaActions}を生成します。
 *
 *
 * <p>
 * <li>サガステップ:</li>
 * </p>
 *
 * - {@link ISagaStep}: サガステップのベースインターフェースとして機能し, 応答が成功であるか, dataに対するAction,
 * 補償処理の有無をチェックするための抽象メソッドを提供します。 {@link SagaStep}がこのインターフェースを拡張しています。
 *
 * - {@link SagaStep}:{@link ISagaStep}に加えて, 応答メッセージに基づいたハンドラの取得,
 * ステップの実行結果の生成を実装する必要があります。
 *
 * - この{@link SagaStep}を実装するクラスは,
 * {@link LocalStep}や{@link ParticipantInvocationStep}があります。
 *
 * - {@link LocalStep}はローカルで実行されるサガステップを表す ローカルステップには応答ハンドラがないため, emptyを返す.
 * ステップの実行結果の生成では, {@link StepOutcome.LocalStepOutcome}を生成します。
 * compensatingがtrueの場合は, 補償処理, そうでない場合は, ローカル処理を実行します。 例外処理も登録済みのものがあれば,
 * それを実行し, 例外を基に, {@link StepOutcome.LocalStepOutcome}を生成します。
 * saveできない例外が発生した場合は, その例外をスローします。
 *
 * - {@link ParticipantInvocationStep}は外部サービスのコマンドや補償を定義するステップを表します。 応答ハンドラでは,
 * リプライクラス(リプライタイプ)に基づいて, 応答ハンドラを取得します。 ステップの実行結果の生成では,
 * {@link StepOutcome.RemoteStepOutcome}を生成します。 compensatingがtrueの場合は, 補償処理,
 * そうでない場合は, リモート処理を実行します。 {@link List<CommandWithDestinationAndType>}を生成し,
 * これを基に, {@link StepOutcome.RemoteStepOutcome}を生成します。
 *
 * - {@link SimpleSagaDefinitionBuilder}は{@link SagaStep}のリストを保持し,
 * {@link SimpleSagaDefinition}を生成します。
 * {@link SimpleSagaDefinition}は{@link SagaDefinition}を実装し, サガの全体の流れを管理するため,
 * ユーザーが定義するSagaでの, {@link #getSagaDefinition}では,
 * コンストラクタで生成した{@link SimpleSagaDefinitionBuilder}でのビルド結果を返すことが可能.
 *
 * {@link SimpleSagaDefinition}は{@link AbstractSimpleSagaDefinition}を継承しており,
 * さらに{@link AbstractSimpleSagaDefinition}は
 * {@link AbstractSagaActionsProvider}を継承しています。
 *
 * - {@link AbstractSagaActionsProvider}の提供メソッドなどは上記の通りで,
 * {@link AbstractSimpleSagaDefinition}は{@link SagaExecutionState}の管理や,
 * {@link SagaActions}の生成を行います。
 *
 * {@link SimpleSagaDefinition}で{@link SagaDefinition}のメソッドとして呼ばれる{@link #start}と{@link #handleReply}の実装について
 * {@link #start}では, {@link #firstStepToExecute}を呼び出し, 初めのアクションを決定します。
 * {@link #handleReply}では, ステップを順に実行し, 応答を処理します。stateの数がステップ数を超えた場合は,
 * エンドステートを返すことでサーガの終了を示します。
 *
 *
 * <p>
 * <b>サガDSL:</b>
 * </p>
 *
 * - {@link SimpleSagaDsl}: シンプルなサガDSL（ドメイン固有言語）を提供するインターフェースです。 step()メソッドを提供し,
 * {@link SimpleSagaDefinitionBuilder}を生成後, これをコンストラクタに渡して,
 * 生成した{@link StepBuilder}を返します。 -
 * {@link SimpleSaga}がこのインターフェースと{@link Saga}を拡張しており, ユーザーはこれを実装することにより,
 * このパッケージを利用して定義した{@link SagaDefinition}を用いて, {@link Saga}を実装することが可能です。
 *
 * - {@link StepBuilder}:
 * サガDSLの各ステップをビルドするための主要なビルダークラスです。ローカルおよび外部呼び出しのステップの構築をサポートします。
 * {@link WithCompensationBuilder}を実装しており, これは条件付き, なし, コマンドエンドポイントを利用,
 * 利用しないの2*2の組み合わせの補償コマンド生成の実装が必要になる. また,
 * ここでの返り値は{@link InvokeParticipantStepBuilder}となり,
 * これもまた{@link WithCompensationBuilder}を実装している.
 *
 * - {@link StepBuilder}は{@link SimpleSagaDefinitionBuilder}を保持し, このビルダーを利用して,
 * ローカルでのアクションを実行するステップ({@link LocalStepBuilder}),
 * パーティシパントにアクションを実行させるステップ({@link InvokeParticipantStepBuilder#withAction}),
 * 条件に応じてパーティシパントにアクションを実行させるステップ({@link InvokeParticipantStepBuilder#withAction}),
 * 特定のエンドポイントに対してパーティシパントにアクションを実行させるステップ({@link InvokeParticipantStepBuilder#withAction}),
 * 条件に応じて、特定のエンドポイントに対してパーティシパントにアクションを実行させるステップ({@link InvokeParticipantStepBuilder#withAction}),
 * パーティシパントに対して補償（コンペンセーション）を実行するステップ({@link InvokeParticipantStepBuilder#withCompensation}),
 * 条件に応じて、パーティシパントに対して補償（コンペンセーション）を実行するステップ({@link InvokeParticipantStepBuilder#withCompensation}),
 * 特定のエンドポイントに対してパーティシパントに対して補償（コンペンセーション）を実行するステップ({@link InvokeParticipantStepBuilder#withCompensation}),
 * 条件に応じて、特定のエンドポイントに対してパーティシパントに対して補償（コンペンセーション）を実行するステップ({@link InvokeParticipantStepBuilder#withCompensation}),
 * パーティシパントに通知を送信するステップ({@link InvokeParticipantStepBuilder#withNotificationAction}),
 * 条件に応じて、パーティシパントに通知を送信するステップ({@link InvokeParticipantStepBuilder#withNotificationAction}),
 * が実装されており, それぞれは生成されたビルダーごとの処理に委ねる.
 *
 * - {@link InvokeParticipantStepBuilder}でも上記のようなことが可能である上に.
 * {@link InvokeParticipantStepBuilder#onReply}での返信ハンドラの追加が可能である.
 *
 * - {@link LocalStepBuilder}では, ローカルステップの生成と設定を行うビルダークラスで,
 * 補償処理の設定である{@link LocalStepBuilder#withCompensation}や例外処理の設定である{@link LocalStepBuilder#withExceptionSaver}を設定可能,
 *
 * - {@link StepBuilder}は, それぞれのビルダをインスタンス化して, それを返すのに対し,
 * {@link LocalStepBuilder}, {@link InvokeParticipantStepBuilder}は自身のインスタンスのactionやcompensationを設定した後,
 * 自身を返す. ただし, これらは, buildとstepメソッドを持ち, どちらもまず, builderに対して, ステップを追加する.
 * 追加されるステップは, {@link LocalStepBuilder}の場合は, {@link LocalStep}を生成し,
 * {@link InvokeParticipantStepBuilder}の場合は,
 * {@link ParticipantInvocationStep}となる.これをbuilderに追加した後, buildメソッドでは,
 * builderの親のbuildメソッドの呼び出しにより, {@link SimpleSagaDefinition}を生成し,
 * そのインスタンスを返す.stepメソッドの場合は, builderへの自身を追加後,
 * {@link StepBuilder}をbuilderを使用して生成し, そのインスタンスを返す. これにより, メソッドチェーンを利用して,
 * ステップを追加することが可能となる.
 *
 */
package org.cresplanex.core.saga.simpledsl;
