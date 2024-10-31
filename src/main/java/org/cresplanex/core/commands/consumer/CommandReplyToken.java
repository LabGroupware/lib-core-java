package org.cresplanex.core.commands.consumer;

import java.util.Map;

/**
 * コマンド返信に使用するトークン情報を保持するクラス。 返信先チャネルと対応するヘッダーを含みます。
 */
public class CommandReplyToken {

    private Map<String, String> replyHeaders;
    private String replyChannel;

    /**
     * 指定された返信ヘッダーと返信チャネルで新しいインスタンスを作成します。
     *
     * @param correlationHeaders 返信ヘッダーのマッピング
     * @param replyChannel 返信先チャネル
     */
    public CommandReplyToken(Map<String, String> correlationHeaders, String replyChannel) {
        this.replyHeaders = correlationHeaders;
        this.replyChannel = replyChannel;
    }

    /**
     * 返信ヘッダーを取得します。
     *
     * @return 返信ヘッダーのマッピング
     */
    public Map<String, String> getReplyHeaders() {
        return replyHeaders;
    }

    /**
     * 返信先チャネルを取得します。
     *
     * @return 返信先チャネル
     */
    public String getReplyChannel() {
        return replyChannel;
    }

    /**
     * 返信ヘッダーを設定します。
     *
     * @param replyHeaders 返信ヘッダーのマッピング
     */
    public void setReplyHeaders(Map<String, String> replyHeaders) {
        this.replyHeaders = replyHeaders;
    }

    /**
     * 返信先チャネルを設定します。
     *
     * @param replyChannel 返信先チャネル
     */
    public void setReplyChannel(String replyChannel) {
        this.replyChannel = replyChannel;
    }
}
