package org.cresplanex.core.common.kafka.baskpressure;

/**
 * バックプレッシャーの状態とアクションのペアを表現するクラス。
 * <p>
 * バックプレッシャー処理におけるアクションとその後の状態を管理します。
 * </p>
 */
public class BackPressureManagerStateAndActions {

    /** 現在のバックプレッシャーアクション */
    final BackPressureActions actions;

    /** 次のバックプレッシャー管理状態 */
    final BackPressureManagerState state;

    /**
     * 指定されたアクションと状態で初期化します。
     *
     * @param actions 現在のアクション
     * @param state 次のバックプレッシャー状態
     */
    public BackPressureManagerStateAndActions(BackPressureActions actions, BackPressureManagerState state) {
        this.actions = actions;
        this.state = state;
    }

    /**
     * アクションなしで指定された状態で初期化します。
     *
     * @param state 次のバックプレッシャー状態
     */
    public BackPressureManagerStateAndActions(BackPressureManagerState state) {
        this(BackPressureActions.NONE, state);
    }
}
