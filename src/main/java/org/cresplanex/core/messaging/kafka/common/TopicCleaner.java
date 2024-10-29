package org.cresplanex.core.messaging.kafka.common;

/**
 * トピック名をクリーニングするユーティリティクラス。
 * <p>
 * 特定の文字列をKafkaトピック名として使用できる形式に変換します。
 * </p>
 */
public class TopicCleaner {

    /**
     * トピック名に含まれる特定の文字を置換して、Kafkaで使用できる形式に変換します。
     * <p>
     * 具体的には、トピック名内の `$` を `_DLR_` に置換します。
     * </p>
     *
     * @param topic 変換前のトピック名
     * @return 変換後のトピック名
     */
    public static String clean(String topic) {
        return topic.replace("$", "_DLR_");
    }
}
