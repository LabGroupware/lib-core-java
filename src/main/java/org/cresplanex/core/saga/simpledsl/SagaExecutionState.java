package org.cresplanex.core.saga.simpledsl;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * サガの実行状態を表すクラスです。
 */
public class SagaExecutionState {

    private int currentlyExecuting;
    private boolean compensating;
    private boolean endState;
    private boolean failed;

    /**
     * 初期状態を生成します。
     *
     * @return 新規作成された開始状態
     */
    public static SagaExecutionState startingState() {
        return new SagaExecutionState(-1, false);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public SagaExecutionState() {
    }

    /**
     * コンストラクタ。
     *
     * @param currentlyExecuting 現在実行中のステップ
     * @param compensating 補償実行中かどうか
     */
    public SagaExecutionState(int currentlyExecuting, boolean compensating) {
        this.currentlyExecuting = currentlyExecuting;
        this.compensating = compensating;
    }

    public int getCurrentlyExecuting() {
        return currentlyExecuting;
    }

    public void setCurrentlyExecuting(int currentlyExecuting) {
        this.currentlyExecuting = currentlyExecuting;
    }

    public boolean isCompensating() {
        return compensating;
    }

    public void setCompensating(boolean compensating) {
        this.compensating = compensating;
    }

    /**
     * 補償を開始するための新しい状態を作成します。
     *
     * @return 補償実行中の状態
     */
    public SagaExecutionState startCompensating() {
        return new SagaExecutionState(currentlyExecuting, true);
    }

    /**
     * 次のステートに進む新しい状態を作成します。
     *
     * @param size ステップの数
     * @return 新しい状態
     */
    public SagaExecutionState nextState(int size) {
        return new SagaExecutionState(compensating ? currentlyExecuting - size : currentlyExecuting + size, compensating);
    }

    public boolean isEndState() {
        return endState;
    }

    public void setEndState(boolean endState) {
        this.endState = endState;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isFailed() {
        return failed;
    }

    /**
     * 終了状態を作成します。
     *
     * @return 終了状態
     */
    public static SagaExecutionState makeEndState() {
        SagaExecutionState x = new SagaExecutionState();
        x.setEndState(true);
        return x;
    }

    /**
     * 失敗終了状態を作成します。
     *
     * @return 失敗した終了状態
     */
    public static SagaExecutionState makeFailedEndState() {
        SagaExecutionState x = new SagaExecutionState();
        x.setEndState(true);
        x.setFailed(true);
        return x;
    }
}
