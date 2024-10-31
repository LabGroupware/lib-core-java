/**
 * This package contains the classes that are used to implement the commands that are used by the consumer.
 *
 * <p>
 * このパッケージでの成果物は, {@link CommandDispatcherFactory} と
 * {@link CommandDispatcher}, {@link CommandHandlersBuilder}, {@link CommandHandlers}
 * である. である.
 *
 * まず, {@link CommandHandlersBuilder}で {@link CommandHandlers} を生成し,
 * {@link CommandDispatcherFactory} で {@link CommandDispatcher} を生成するという流れとなる.
 *
 * {@link CommandHandler} は, {@link AbstractCommandHandler} を継承し,
 * 指定されたコマンドクラスに応じて処理を行う.
 *
 * {@link AbstractCommandHandler} は, チャンネル, リソースパス,
 * コマンドタイプ(コマンドクラス名でもある)に基づいてメッセージの処理を定義するためのクラスである. handlesメソッドは,
 * メッセージがこのハンドラーによって処理可能かを確認する.具体的には, コマンドタイプが一致し,
 * リソースが一致(ハンドラ側の定義でプレースホルダーやempty(全て)の設定も可能)するかを確認する. {@link CommandArgs}
 * を引数に取り, 処理を行い, RESULTを返す関数がhandlerに渡される. {@link CommandArgs} は,
 * コマンドメッセージ(型は{@link CommandMessage}<T = Command>となる), パス変数,
 * コマンド返信トークン(リプライヘッダーとリプライチャネル)を含む. {@link CommandMessage} は, メッセージID,
 * コマンドタイプ(クラス)(T = Command), 相関ヘッダー(map), メッセージデータを含む.
 *
 * RESULTをList<Message>で定義したものが {@link CommandHandler} である.
 * {@link CommandHandlers}はこれのリストを管理し,
 * 自身が対応するすべてのチャネルを取得やmassageを基に適切なコマンドハンドラを見つけるメソッドを提供する.
 * {@link CommandHandlersBuilder} は, チャネル,
 * リソース(optional)で定義済みのビルダにcommandClassとhandlerの複数登録を行うことが可能. 登録可能なハンドラには,
 * 以下の種類がある.
 * <ul>
 * <li>onMessage(Class<C> commandClass, Consumer<CommandMessage<C>> handler):
 * CommandMessage<C>を受け取って何も返さないハンドラ</li>
 * <li>onComplexMessage(Class<C> commandClass, BiConsumer<CommandMessage<C>,
 * CommandReplyToken> handler):
 * CommandMessage<C>とCommandReplyTokenを受け取って複雑な処理を行うハンドラ</li>
 * <li>onMessage(Class<C> commandClass, Function<CommandMessage<C>, Message>
 * handler): CommandMessage<C>を受け取って単一のMessageを返すハンドラ</li>
 * <li>onMessageReturningOptionalMessage(Class<C> commandClass,
 * Function<CommandMessage<C>, Optional<Message>> handler):
 * CommandMessage<C>を受け取ってオプションでMessageを返すハンドラ</li>
 * <li>onMessageReturningMessages(Class<C> commandClass,
 * Function<CommandMessage<C>, List<Message>> handler):
 * CommandMessage<C>を受け取ってList<Message>を返すハンドラ</li>
 * <li>onMessage(Class<C> commandClass, BiFunction<CommandMessage<C>,
 * PathVariables, Message> handler):
 * CommandMessage<C>とPathVariablesを受け取ってMessageを返すハンドラ</li>
 * <li>onMessageReturningOptionalMessage(Class<C> commandClass,
 * BiFunction<CommandMessage<C>, PathVariables, Optional<Message>> handler):
 * CommandMessage<C>とPathVariablesを受け取ってオプションでMessageを返すハンドラ</li>
 * <li>onMessageReturningMessages(Class<C> commandClass,
 * BiFunction<CommandMessage<C>, PathVariables, List<Message>> handler):
 * CommandMessage<C>とPathVariablesを受け取ってList<Message>を返すハンドラ</li>
 *
 * いくつかにMessageを返すとあるが,
 * これは{@link CommandHandlerReplyBuilder}を利用して返信メッセージを生成することができる.
 *
 * {@link CommandDispatcherFactory} は,
 * {@link org.cresplanex.core.messaging.consumer.MessageConsumer}, {@link org.cresplanex.core.commands.common.CommandNameMapping}, {@link CommandReplyProducer}
 * を引数に取り, {@link CommandDispatcher} を生成後, initializeを行う.
 * {@link CommandDispatcher} は, 定義されているmessageHandlerを使用して, subscribeを行う.
 *
 * <ol>
 * <li>コマンドタイプヘッダーを{@link org.cresplanex.core.commands.common.CommandNameMapping}を使用して置き換える.</li>
 * <li>{@link CommandHandlers}から対象メソッドを見つける.</li>
 * <li>{@link CommandHandlerParams}を使用して, {@link CommandReplyToken}を生成する.</li>
 * <li>{@link CommandMessage}を生成する.</li>
 * <li>登録済みの{@link CommandHandler}を実行し, 結果(reply messageのlist)を取得する.</li>
 * <li>{@link CommandReplyProducer}を使用して, 返信メッセージを生成する.</li>
 * <li>Messageと相関ヘッダーを設定し, replyChannelに返信する.</li>
 * </ol>
 *
 * {@link CommandDispatcher}で利用されている{@link CommandReplyProducer}
 * </p>
 */
package org.cresplanex.core.commands.consumer;
