package org.cresplanex.core.common.jdbc;

import java.util.Objects;

/**
 * アウトボックステーブルのサフィックス情報を表すクラス。
 * <p>
 * このクラスは、テーブル名にサフィックスを追加するための整数値とその文字列表現を保持します。
 * </p>
 */
public class OutboxTableSuffix {

    /** テーブルのサフィックスを表す整数値 */
    public final Integer suffix;

    /** サフィックスの文字列表現 */
    public final String suffixAsString;

    /**
     * 指定されたサフィックス値で {@link OutboxTableSuffix} オブジェクトを構築します。
     * <p>
     * サフィックスがnullの場合、文字列表現は空文字列になります。
     * </p>
     *
     * @param suffix テーブルサフィックスとして使用する整数値
     */
    public OutboxTableSuffix(Integer suffix) {
        this.suffix = suffix;
        this.suffixAsString = suffix == null ? "" : this.suffix.toString();
    }

    /**
     * このオブジェクトと指定されたオブジェクトが等しいかを判定します。
     * <p>
     * 同じクラスで、かつ `suffix` および `suffixAsString` が等しい場合にtrueを返します。
     * </p>
     *
     * @param o 比較対象のオブジェクト
     * @return オブジェクトが等しい場合は true、そうでない場合は false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutboxTableSuffix that = (OutboxTableSuffix) o;
        return Objects.equals(suffix, that.suffix) && suffixAsString.equals(that.suffixAsString);
    }

    /**
     * このオブジェクトのハッシュコードを返します。
     *
     * @return `suffix` と `suffixAsString` に基づくハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hash(suffix, suffixAsString);
    }
}
