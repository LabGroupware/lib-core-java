package org.cresplanex.core.common.kafka.common;

import java.nio.charset.Charset;

/**
 * バイナリメッセージのエンコーディングとデコーディングを行うユーティリティクラス。
 * <p>
 * バイト配列を文字列に変換、またはその逆の変換を提供します。
 * </p>
 */
public class CoreBinaryMessageEncoding {

    /**
     * バイト配列をUTF-8形式の文字列に変換します。
     *
     * @param bytes 変換するバイト配列
     * @return 変換後の文字列
     */
    public static String bytesToString(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }

    /**
     * 文字列をUTF-8形式のバイト配列に変換します。
     *
     * @param string 変換する文字列
     * @return 変換後のバイト配列
     */
    public static byte[] stringToBytes(String string) {
        return string.getBytes(Charset.forName("UTF-8"));
    }
}
