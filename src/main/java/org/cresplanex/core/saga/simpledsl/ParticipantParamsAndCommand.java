package org.cresplanex.core.saga.simpledsl;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;

/**
 * 参加者のパラメータとコマンドを格納するクラスです。
 *
 * @param <C> コマンドのタイプ
 */
public class ParticipantParamsAndCommand<C extends Command> {

    private final Map<String, String> params;
    private final C command;

    /**
     * コンストラクタ。
     *
     * @param params コマンドに関連するパラメータ
     * @param command 実行するコマンド
     */
    public ParticipantParamsAndCommand(Map<String, String> params, C command) {
        this.params = params;
        this.command = command;
    }

    /**
     * パラメータを取得します。
     *
     * @return コマンドに関連するパラメータ
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * コマンドを取得します。
     *
     * @return 実行するコマンド
     */
    public C getCommand() {
        return command;
    }
}
