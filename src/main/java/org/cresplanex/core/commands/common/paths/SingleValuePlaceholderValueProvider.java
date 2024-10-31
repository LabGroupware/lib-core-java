package org.cresplanex.core.commands.common.paths;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 単一の値を提供するプレースホルダプロバイダー。使用されると一度だけ値を返し、二度目は空のオプションを返します。
 */
public class SingleValuePlaceholderValueProvider implements PlaceholderValueProvider {

    // パスパラメータの単一値
    private final Object pathParam;

    // 値が使用されたかどうかを示すフラグ
    private boolean used;

    /**
     * コンストラクタ。単一のパスパラメータを設定します。
     *
     * @param pathParam パスのプレースホルダに対応する単一の値
     */
    public SingleValuePlaceholderValueProvider(Object pathParam) {
        this.pathParam = pathParam;
    }

    /**
     * プレースホルダ名に対応する値を返します。最初の呼び出しでのみ値を返し、以降は空のOptionalを返します。
     *
     * @param name プレースホルダ名
     * @return 値が設定されていればOptionalで包まれた値、なければOptional.empty()
     */
    @Override
    public Optional<String> get(String name) {
        if (!used) {
            used = true;
            return Optional.of(pathParam.toString());
        } else {
            return Optional.empty();
        }
    }

    /**
     * パラメータのマッピングを返します。
     *
     * @return キー "singleValue" に pathParam の値を持つ Map
     */
    @Override
    public Map<String, String> getParams() {
        return Collections.singletonMap("singleValue", pathParam.toString());
    }
}
