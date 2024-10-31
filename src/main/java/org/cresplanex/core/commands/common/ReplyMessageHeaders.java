package org.cresplanex.core.commands.common;

/**
 * リプライメッセージに関連するヘッダキーを定義するクラス。 リプライの種類や対応するメッセージID、結果の種類などを識別するためのヘッダ情報を提供します。
 */
public class ReplyMessageHeaders {

    /**
     * リプライの種類を表すヘッダ（未使用）
     */
    public static final String REPLY_TYPE = "reply_type"; // Unused??

    /**
     * 対応する元メッセージのIDを示すヘッダ
     */
    public static final String IN_REPLY_TO = "reply_to_message_id";

    /**
     * リプライの結果（成功・失敗など）を示すヘッダ
     */
    public static final String REPLY_OUTCOME = "reply_outcome-type";
}
