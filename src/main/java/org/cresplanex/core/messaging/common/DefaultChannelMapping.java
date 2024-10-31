package org.cresplanex.core.messaging.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ChannelMapping} that uses a map to store the
 * mappings.
 *
 * チャネルをマッピング定義しておき, それに基づいて変換を行う {@link ChannelMapping} のデフォルト実装.
 */
public class DefaultChannelMapping implements ChannelMapping {

    private final Map<String, String> mappings;

    public static class DefaultChannelMappingBuilder {

        private final Map<String, String> mappings = new HashMap<>();

        public DefaultChannelMappingBuilder with(String from, String to) {
            mappings.put(from, to);
            return this;
        }

        public ChannelMapping build() {
            return new DefaultChannelMapping(mappings);
        }
    }

    public static DefaultChannelMappingBuilder builder() {
        return new DefaultChannelMappingBuilder();
    }

    public DefaultChannelMapping(Map<String, String> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String transform(String channel) {
        return mappings.getOrDefault(channel, channel);
    }
}
