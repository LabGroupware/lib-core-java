package org.cresplanex.core.commands.common;

/**
 * コマンドメッセージのヘッダに関する定数を定義するクラス。 メッセージの識別やリプライなどに必要な情報を格納します。
 */
public class CommandMessageHeaders {

    /**
     * コマンドヘッダのプレフィックス
     */
    public static final String COMMAND_HEADER_PREFIX = "command_";

    /**
     * コマンドの種類を表すヘッダ
     */
    public static final String COMMAND_TYPE = COMMAND_HEADER_PREFIX + "type";

    /**
     * リソースを格納するヘッダ
     */
    public static final String RESOURCE = COMMAND_HEADER_PREFIX + "resource";

    /**
     * コマンドの送信先を指定するヘッダ
     */
    public static final String DESTINATION = COMMAND_HEADER_PREFIX + "_destination";

    /**
     * コマンドリプライのプレフィックス
     */
    public static final String COMMAND_REPLY_PREFIX = "commandreply_";

    /**
     * リプライ先を指定するヘッダ
     */
    public static final String REPLY_TO = COMMAND_HEADER_PREFIX + "reply_to";

    /**
     * 指定されたヘッダ名をリプライ用のヘッダに変換します。
     *
     * @param header コマンドヘッダ名
     * @return リプライ用のヘッダ名
     * @throws AssertionError ヘッダがCOMMAND_HEADER_PREFIXで始まらない場合
     */
    public static String inReply(String header) {
        assert header.startsWith(COMMAND_HEADER_PREFIX);
        return COMMAND_REPLY_PREFIX + header.substring(COMMAND_HEADER_PREFIX.length());
    }
}
