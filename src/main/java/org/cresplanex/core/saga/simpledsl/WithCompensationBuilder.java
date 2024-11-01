package org.cresplanex.core.saga.simpledsl;

import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandWithDestination;

/**
 * サガワークフローにおける補償ステップを定義するインターフェース。
 *
 * @param <Data> サガワークフローで使用されるデータ型。
 */
public interface WithCompensationBuilder<Data> {

    /**
     * サガステップの補償アクションを定義します。
     *
     * @param compensation 補償コマンドを生成する関数。
     * @return さらなるステップ設定のためのInvokeParticipantStepBuilderのインスタンス。
     */
    InvokeParticipantStepBuilder<Data> withCompensation(Function<Data, CommandWithDestination> compensation);

    /**
     * サガステップの条件付き補償アクションを定義します。
     *
     * @param compensationPredicate 補償が必要かどうかを判断する述語。
     * @param compensation 補償コマンドを生成する関数。
     * @return さらなるステップ設定のためのInvokeParticipantStepBuilderのインスタンス。
     */
    InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, Function<Data, CommandWithDestination> compensation);

    /**
     * サガステップの補償アクションをコマンドエンドポイントを使用して定義します。
     *
     * @param commandEndpoint 補償コマンドを送信するコマンドエンドポイント。
     * @param commandProvider サガデータに基づいてコマンドを作成する関数。
     * @param <C> 補償のために送信されるコマンドの型。
     * @return さらなるステップ設定のためのInvokeParticipantStepBuilderのインスタンス。
     */
    <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider);

    /**
     * サガステップの条件付き補償アクションをコマンドエンドポイントを使用して定義します。
     *
     * @param compensationPredicate 補償が必要かどうかを判断する述語。
     * @param commandEndpoint 補償コマンドを送信するコマンドエンドポイント。
     * @param commandProvider サガデータに基づいてコマンドを作成する関数。
     * @param <C> 補償のために送信されるコマンドの型。
     * @return さらなるステップ設定のためのInvokeParticipantStepBuilderのインスタンス。
     */
    <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider);
}
