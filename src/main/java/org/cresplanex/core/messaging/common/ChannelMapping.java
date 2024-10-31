package org.cresplanex.core.messaging.common;

/**
 * Interface for mapping channels.
 *
 * チャネルをマッピングするためのインターフェース.
 */
public interface ChannelMapping {

    /**
     * Transforms the given channel.
     *
     * 指定されたチャネルを変換します.
     *
     * @param channel the channel to transform
     * @return the transformed channel
     */
    String transform(String channel);
}
