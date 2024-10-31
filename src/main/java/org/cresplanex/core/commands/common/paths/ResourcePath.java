package org.cresplanex.core.commands.common.paths;

import java.util.Arrays;
import static java.util.stream.Collectors.joining;

/**
 * リソースパスを表現するクラス。 パスの分割や結合、長さの取得などの操作を提供します。
 */
public class ResourcePath {

    // パスのセグメント配列
    final String[] splits;

    /**
     * 分割されたパスの配列を受け取ってインスタンスを生成します。
     *
     * @param splits 分割されたパスの配列
     */
    public ResourcePath(String[] splits) {
        this.splits = splits;
    }

    /**
     * 文字列リソースを"/"で分割してインスタンスを生成します。
     *
     * @param resource "/"で始まるリソースパス
     * @throws IllegalArgumentException パスが"/"で始まらない場合に発生
     */
    public ResourcePath(String resource) {
        if (!resource.startsWith("/")) {
            throw new IllegalArgumentException("Should start with / " + resource);
        }
        this.splits = splitPath(resource);
    }

    /**
     * パスを"/"で分割してセグメント配列を生成します。
     *
     * @param path 分割するパス文字列
     * @return 分割されたパスのセグメント配列
     */
    private String[] splitPath(String path) {
        return path.split("/");
    }

    /**
     * 指定されたリソースパスを解析してResourcePathオブジェクトを生成します。
     *
     * @param resource リソースパス
     * @return 新しいResourcePathインスタンス
     */
    public static ResourcePath parse(String resource) {
        return new ResourcePath(resource);
    }

    /**
     * パスの長さ（セグメントの数）を返します。
     *
     * @return セグメントの数
     */
    public int length() {
        return splits.length;
    }

    /**
     * セグメントを"/"で結合して完全なパス文字列を返します。
     *
     * @return パス文字列
     */
    public String toPath() {
        return Arrays.stream(splits).collect(joining("/"));
    }
}
