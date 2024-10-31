package org.cresplanex.core.commands.consumer;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * リプライ先の宛先情報を表すクラス。 メッセージの送信先の情報とパーティションキーを含みます。
 */
public class ReplyDestination {

    /**
     * リプライメッセージの宛先を示す文字列
     */
    public final String destination;

    /**
     * パーティションキーを示す文字列
     */
    public final String partitionKey;

    /**
     * 指定された宛先とパーティションキーで {@code ReplyDestination} のインスタンスを生成します。
     *
     * @param destination リプライメッセージの宛先
     * @param partitionKey パーティションキー
     */
    public ReplyDestination(String destination, String partitionKey) {
        this.partitionKey = partitionKey;
        this.destination = destination;
    }

    /**
     * オブジェクトの文字列表現を返します。
     *
     * @return オブジェクトの文字列表現
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
