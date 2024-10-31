package org.cresplanex.core.commands.consumer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.paths.ResourcePath;
import org.cresplanex.core.commands.common.paths.ResourcePathPattern;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;

/**
 * コマンドハンドラのパラメータを管理するクラス。 メッセージからコマンド、パス変数、相関ヘッダー、デフォルトの返信チャネルなどを抽出し保持します。
 */
public class CommandHandlerParams {

    private final Object command;
    private final Map<String, String> correlationHeaders;
    private final Map<String, String> pathVars;
    private final Optional<String> defaultReplyChannel;

    /**
     * メッセージから CommandHandlerParams のインスタンスを作成します。
     *
     * @param message 処理対象のメッセージ
     * @param commandClass コマンドのクラスタイプ
     * @param resource コマンドに関連付けられたリソースのパス（オプション）
     */
    public CommandHandlerParams(Message message, Class<?> commandClass, Optional<String> resource) {
        command = JSonMapper.fromJson(message.getPayload(), commandClass);
        pathVars = getPathVars(message, resource);
        correlationHeaders = getCorrelationHeaders(message.getHeaders());
        defaultReplyChannel = message.getHeader(CommandMessageHeaders.REPLY_TO); // リプライチャネルをデフォルトの返信チャネルとして設定
    }

    /**
     * コマンドオブジェクトを取得します。
     *
     * @return コマンドオブジェクト
     */
    public Object getCommand() {
        return command;
    }

    /**
     * 相関ヘッダーを取得します。
     *
     * @return 相関ヘッダーのマップ
     */
    public Map<String, String> getCorrelationHeaders() {
        return correlationHeaders;
    }

    /**
     * パス変数を取得します。
     *
     * @return パス変数のマップ
     */
    public Map<String, String> getPathVars() {
        return pathVars;
    }

    /**
     * デフォルトの返信チャネルを取得します。
     *
     * @return デフォルトの返信チャネル（オプション）
     */
    public Optional<String> getDefaultReplyChannel() {
        return defaultReplyChannel;
    }

    /**
     * メッセージのヘッダーから相関ヘッダーを抽出します。
     *
     * @param headers メッセージの全ヘッダー
     * @return 相関ヘッダーのマップ
     */
    private Map<String, String> getCorrelationHeaders(Map<String, String> headers) {
        // コマンドヘッダーを基にリプライヘッダーを生成(キーのプレフィックスを変更)
        // Replyにそのまま入るのはCOMMAND_TYPE, RESOURCE, DESTINATION, REPLY_TO
        Map<String, String> m = headers.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(CommandMessageHeaders.COMMAND_HEADER_PREFIX))
                .collect(Collectors.toMap(e -> CommandMessageHeaders.inReply(e.getKey()),
                        Map.Entry::getValue));
        // 追加でIN_REPLY_TOをどのメッセージに対するリプライかを示すために追加
        m.put(ReplyMessageHeaders.IN_REPLY_TO, headers.get(Message.ID));
        return m;
    }

    /**
     * メッセージからパス変数を抽出します。
     *
     * ハンドラのパスが/abc/{id}/{u}の場合、メッセージのリソースが/abc/123/helloの場合、id=123,
     * u=helloとなります。
     *
     * @param message メッセージ
     * @param handlerResource ハンドラに関連付けられたリソース（オプション）
     * @return パス変数のマップ
     */
    private Map<String, String> getPathVars(Message message, Optional<String> handlerResource) {
        return handlerResource.flatMap(res -> {
            ResourcePathPattern r = ResourcePathPattern.parse(res);
            return message.getHeader(CommandMessageHeaders.RESOURCE).map(h -> {
                ResourcePath mr = ResourcePath.parse(h);
                return r.getPathVariableValues(mr);
            });
        }).orElse(Collections.emptyMap());
    }
}
