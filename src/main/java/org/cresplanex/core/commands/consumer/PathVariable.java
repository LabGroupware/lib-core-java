package org.cresplanex.core.commands.consumer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * パス変数としてマッピングされるフィールドやメソッドパラメータを示す注釈。 HTTP リクエストの URI パスから変数の値を取得する際に使用されます。
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {

    /**
     * パス変数の名前を指定します。
     *
     * @return パス変数の名前
     */
    String value();
}
