/**
 * This package contains the classes that are used to define the paths for the commands.
 *
 * {@link PlaceholderValueProvider}では, プレースホルダと値とのマッピングを提供するインターフェースが定義されている.
 * 実装クラスとして,
 * {@link PlaceholderValueMapProvider}と{@link SingleValuePlaceholderValueProvider}が提供されており,
 * それぞれマップと単一の値を提供.
 *
 * {@link RequestPath}は, リクエストパスを表すクラスであり, パスのセグメントを取得するためのメソッドを提供している. 主に,
 * '/'で区切られた文字列と配列を相互変換するためのメソッドが提供されている.
 *
 * PlaceHolderは'{'で始まり, '}'で終わる文字列で有ることが想定されている. /aaa/{bbb}/ccc/{d}
 * のようなパスパターンに対して, ResourcePathオブジェクトが /aaa/123/ccc/456 の場合, プレースホルダ名 bbb と値
 * 123, プレースホルダ名 d と値 456 となる.
 * 
 * {@link ResourcePathPattern}では, パターン一致の判定, ２つの{@link ResourcePath}のプレースホルダに対応するパス変数の値を取得,
 * {@link PlaceholderValueProvider}もしくは, 置き換え配列を用いたプレースホルダーの置き換えを行うメソッドが提供されている.
 */
package org.cresplanex.core.commands.common.paths;
