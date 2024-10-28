package org.cresplanex.core.common.jdbc;

/**
 * JDBC接続URLの情報を保持するクラス。
 * <p>
 * データベース接続に必要なホスト、ポート、およびデータベース名の情報を含みます。
 * </p>
 */
public class JdbcUrl {

    /**
     * 接続先のホスト名
     */
    String host;

    /**
     * 接続先のポート番号
     */
    int port;

    /**
     * 接続先のデータベース名
     */
    String database;

    /**
     * JdbcUrlのコンストラクタ。
     *
     * @param host 接続先のホスト名
     * @param port 接続先のポート番号
     * @param database 接続先のデータベース名
     */
    public JdbcUrl(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

    /**
     * ホスト名を取得します。
     *
     * @return ホスト名
     */
    public String getHost() {
        return host;
    }

    /**
     * ポート番号を取得します。
     *
     * @return ポート番号
     */
    public int getPort() {
        return port;
    }

    /**
     * データベース名を取得します。
     *
     * @return データベース名
     */
    public String getDatabase() {
        return database;
    }
}
