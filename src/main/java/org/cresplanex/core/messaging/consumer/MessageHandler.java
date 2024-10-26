package org.cresplanex.core.messaging.consumer;

import java.util.function.Consumer;

import org.cresplanex.core.messaging.common.Message;

/**
 * Interface for handling messages.
 *
 * メッセージを処理するためのインターフェース.
 * サブスクリプション時にこの実装を渡すことで、メッセージを受信するための処理を行います.
 */
public interface MessageHandler extends Consumer<Message> {
}
