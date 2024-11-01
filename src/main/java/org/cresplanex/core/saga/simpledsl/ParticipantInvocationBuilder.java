package org.cresplanex.core.saga.simpledsl;

import static java.util.Collections.singletonMap;
import java.util.Map;

import org.cresplanex.core.commands.common.Command;

/**
 * 参加者のインボケーションを作成するためのビルダークラス。
 */
public class ParticipantInvocationBuilder {

    private final Map<String, String> params;

    /**
     * コンストラクタ。
     *
     * @param key パラメータのキー
     * @param value パラメータの値
     */
    public ParticipantInvocationBuilder(String key, String value) {
        this.params = singletonMap(key, value);
    }

    /**
     * コマンドを含むインボケーションを生成します。
     *
     * @param command コマンド
     * @param <C> コマンドのタイプ
     * @return ParticipantParamsAndCommand インボケーションに必要なパラメータとコマンド
     */
    public <C extends Command> ParticipantParamsAndCommand<C> withCommand(C command) {
        return new ParticipantParamsAndCommand<>(params, command);
    }
}
