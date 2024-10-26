package org.cresplanex.core.messaging.consumer;

/**
 * Order of built-in message handler decorators.
 * 
 * 組み込みメッセージハンドラデコレータの順序.
 */
public class BuiltInMessageHandlerDecoratorOrder {
  // メッセージの受信前/後ハンドラデコレータ
  public static int PRE_POST_RECEIVE_MESSAGE_HANDLER_DECORATOR = 100;
  // 重複検出メッセージハンドラデコレータ
  public static int DUPLICATE_DETECTING_MESSAGE_HANDLER_DECORATOR = 200;
  // メッセージの処理前/後ハンドラデコレータ
  public static int PRE_POST_HANDLER_MESSAGE_HANDLER_DECORATOR = 300;
}
