package org.cresplanex.core.commands.common;

/**
 * デフォルトのコマンド名マッピングを実装するクラス。 コマンドのクラス名を外部表現としてそのまま使用します。
 */
public class DefaultCommandNameMapping implements CommandNameMapping {

    /**
     * コマンドオブジェクトから外部のコマンドタイプ文字列に変換します。
     *
     * @param command コマンドオブジェクト
     * @return コマンドのクラス名を表す文字列
     */
    @Override
    public String commandToExternalCommandType(Command command) {
        return command.getClass().getName();
    }

    /**
     * 外部のコマンドタイプ文字列からコマンドクラス名を取得します。
     *
     * @param commandTypeHeader 外部コマンドタイプのヘッダ
     * @return コマンドクラス名の文字列
     */
    @Override
    public String externalCommandTypeToCommandClassName(String commandTypeHeader) {
        return commandTypeHeader;
    }
}
