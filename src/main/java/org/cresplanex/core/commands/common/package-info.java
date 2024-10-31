/**
 * This package contains common commands that are used by multiple modules.
 *
 * {@link Command}では, コマンドを表現するインターフェースが定義されており, 全てのコマンドはこのインターフェースを実装する.
 *
 * {@link CommandMessageHeaders}, {@link ReplyMessageHeaders}でヘッダーのキーを定義し,
 * REPLYヘッダーへの変換メソッドを提供.このうちのTYPEヘッダーと実際のクラスネームのマッピングは,
 * {@link CommandNameMapping}で行われ, この実装は{@link DefaultCommandNameMapping}で行われる.
 *
 * コマンドの結果を表す{@link Outcome}インターフェースを実装した{@link Success}と{@link Failure}が提供されている.
 * また, {@link CommandReplyOutcome}では,
 * リプライ時の{@link Success}, {@link Failure}のみの列挙型として定義されている.
 */
package org.cresplanex.core.commands.common;
