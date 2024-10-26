package org.cresplanex.core.commands.producer;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;

public interface CommandProducer {

    /**
     * Sends a command
     *
     * @param channel the channel to send the command to
     * @param command the command to send
     * @param replyTo the channel to send the reply to
     * @param headers additional headers
     * @return the id of the sent command
     */
    String send(String channel, Command command, String replyTo,
            Map<String, String> headers);

    /**
     * Sends a command
     *
     * @param channel the channel to send the command to
     * @param command the command to send
     * @param headers additional headers
     * @return the id of the sent command
     */
    String sendNotification(String channel, Command command,
            Map<String, String> headers);

    /**
     * Sends a command
     *
     * @param channel the channel to send the command to
     * @param resource
     * @param command the command to send
     * @param replyTo the channel to send the reply to
     * @param headers additional headers
     * @return the id of the sent command
     */
    String send(String channel, String resource, Command command, String replyTo, Map<String, String> headers);
}
