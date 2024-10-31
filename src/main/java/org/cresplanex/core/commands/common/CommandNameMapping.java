package org.cresplanex.core.commands.common;

/**
 * コマンドとその外部表現との間での変換方法を定義するインターフェース。
 * コマンドの種類を外部表現に変換したり、外部表現からコマンドクラス名を取得する機能を提供します。
 */
public interface CommandNameMapping {

    /**
     * コマンドをコマンドヘッダで利用されるTypeに変換します。
     *
     * @param command コマンドオブジェクト
     * @return コマンドヘッダで利用されるTypeName
     */
    String commandToExternalCommandType(Command command);

    /**
     * コマンドヘッダで利用されるTypeからコマンドクラス名を取得します。
     *
     * @param commandTypeHeader 外部コマンドタイプのヘッダ
     * @return 対応するコマンドクラス名
     */
    String externalCommandTypeToCommandClassName(String commandTypeHeader);
}
