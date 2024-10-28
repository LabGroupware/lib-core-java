package org.cresplanex.core.common.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC接続URLを解析して {@link JdbcUrl} オブジェクトを生成するためのユーティリティクラス。
 * <p>
 * 接続URLからホスト名、ポート番号、データベース名を抽出し、適切に {@link JdbcUrl} インスタンスを構築します。
 * </p>
 */
public class JdbcUrlParser {

    /**
     * JDBC接続URLを解析し、ホスト名、ポート番号、およびデータベース名を含む {@link JdbcUrl} オブジェクトを生成します。
     *
     * @param dataSourceURL JDBC接続URL（例:
     * "jdbc:mysql://localhost:3306/mydatabase"）
     * @return 解析された {@link JdbcUrl} インスタンス
     * @throws RuntimeException URLが無効な形式の場合に発生します
     */
    public static JdbcUrl parse(String dataSourceURL) {
        Pattern p = Pattern.compile("jdbc:[a-zA-Z0-9]+://([^:/]+)(:[0-9]+)?/([^?]+)(\\?.*)?$");
        Matcher m = p.matcher(dataSourceURL);

        if (!m.matches()) {
            throw new RuntimeException(dataSourceURL);
        }

        String host = m.group(1);
        String port = m.group(2);
        String database = m.group(3);
        return new JdbcUrl(host, port == null ? 3306 : Integer.parseInt(port.substring(1)), database);
    }
}
