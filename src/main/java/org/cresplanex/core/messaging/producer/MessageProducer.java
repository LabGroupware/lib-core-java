package org.cresplanex.core.messaging.producer;

import org.cresplanex.core.messaging.common.Message;

/**
 * Interface for sending messages.
 *
 * メッセージを送信するためのインターフェース.
 * プロデューサーはこれを実装することで、メッセージを送信できます.
 */
public interface MessageProducer {

  /**
   * Send a message
   * @param destination the destination channel
   * @param message the message to doSend
   * @see Message
   */
  void send(String destination, Message message);

}
