package org.cresplanex.core.saga.orchestration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cresplanex.core.saga.orchestration.command.CommandWithDestinationAndType;

/**
 * サーガの一連のアクションを表すクラス。 サーガの現在の状態や更新データ、送信するコマンドを含み、ビルダーでの設定も可能です。
 *
 * @param <Data> サーガのデータの型
 */
public class SagaActions<Data> {

    private final List<CommandWithDestinationAndType> commands;
    private final Optional<Data> updatedSagaData;
    private final Optional<String> updatedState;
    private final boolean endState;
    private final boolean compensating;
    private final boolean local;
    private final Optional<RuntimeException> localException;
    private final boolean failed;

    /**
     * コンストラクタ。サーガのアクションの各設定項目を受け取ります。
     */
    public SagaActions(List<CommandWithDestinationAndType> commands,
            Optional<Data> updatedSagaData,
            Optional<String> updatedState, boolean endState, boolean compensating, boolean failed, boolean local, Optional<RuntimeException> localException) {
        this.commands = commands;
        this.updatedSagaData = updatedSagaData;
        this.updatedState = updatedState;
        this.endState = endState;
        this.compensating = compensating;
        this.local = local;
        this.localException = localException;
        this.failed = failed;
    }

    /**
     * 送信するコマンドのリストを取得します。
     */
    public List<CommandWithDestinationAndType> getCommands() {
        return commands;
    }

    /**
     * 更新されたサーガのデータを取得します。
     */
    public Optional<Data> getUpdatedSagaData() {
        return updatedSagaData;
    }

    /**
     * 更新されたサーガの状態名を取得します。
     */
    public Optional<String> getUpdatedState() {
        return updatedState;
    }

    /**
     * サーガが終了状態かどうかを示すフラグを取得します。
     */
    public boolean isEndState() {
        return endState;
    }

    /**
     * サーガが補償トランザクション中かどうかを示すフラグを取得します。
     */
    public boolean isCompensating() {
        return compensating;
    }

    /**
     * サーガが応答を期待しているかどうかを判定します。
     */
    public boolean isReplyExpected() {
        // 送信するコマンドがない, もしくは一つでも一方的な通知ではなく応答が必要なコマンドである場合, かつローカルでない場合はtrue
        // ex. 送信するコマンドが存在し, localでない場合: true
        return (commands.isEmpty() || commands.stream().anyMatch(CommandWithDestinationAndType::isCommand)) && !local;
    }

    /**
     * サーガが失敗状態かどうかを示すフラグを取得します。
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * ローカルで発生した例外がある場合、その例外を取得します。
     */
    public Optional<RuntimeException> getLocalException() {
        return localException;
    }

    /**
     * SagaActionsのビルダー。サーガのアクションを構築するためのメソッドを提供します。
     */
    public static class Builder<Data> {

        private final List<CommandWithDestinationAndType> commands = new ArrayList<>();
        private Optional<Data> updatedSagaData = Optional.empty();
        private Optional<String> updatedState = Optional.empty();
        private boolean endState;
        private boolean compensating;
        private boolean local;
        private boolean failed;
        private Optional<RuntimeException> localException = Optional.empty();

        public Builder() {
        }

        public SagaActions<Data> build() {
            return new SagaActions<>(commands, updatedSagaData, updatedState, endState, compensating, failed, local, localException);
        }

        public Builder<Data> withCommand(CommandWithDestinationAndType command) {
            commands.add(command);
            return this;
        }

        public Builder<Data> withUpdatedSagaData(Data data) {
            this.updatedSagaData = Optional.of(data);
            return this;
        }

        public Builder<Data> withUpdatedState(String state) {
            this.updatedState = Optional.of(state);
            return this;
        }

        public Builder<Data> withCommands(List<CommandWithDestinationAndType> commands) {
            this.commands.addAll(commands);
            return this;
        }

        public Builder<Data> withIsEndState(boolean endState) {
            this.endState = endState;
            return this;
        }

        public Builder<Data> withIsFailed(boolean failed) {
            this.failed = failed;
            return this;
        }

        public Builder<Data> withIsCompensating(boolean compensating) {
            this.compensating = compensating;
            return this;
        }

        public Builder<Data> withIsLocal(Optional<RuntimeException> localException) {
            this.localException = localException;
            this.local = true;
            return this;
        }

        public SagaActions<Data> buildActions(Data data, boolean compensating, String state, boolean endState) {
            return withUpdatedSagaData(data)
                    .withUpdatedState(state)
                    .withIsEndState(endState)
                    .withIsCompensating(compensating)
                    .build();
        }

    }

    /**
     * 新しいビルダーを作成します。
     */
    public static <Data> Builder<Data> builder() {
        return new Builder<>();
    }

}
