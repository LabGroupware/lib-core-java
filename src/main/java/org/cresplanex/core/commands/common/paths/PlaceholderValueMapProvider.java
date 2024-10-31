package org.cresplanex.core.commands.common.paths;

import java.util.Map;
import java.util.Optional;

/**
 * マップを使用してプレースホルダの値を提供するクラス。
 */
public class PlaceholderValueMapProvider implements PlaceholderValueProvider {

    // プレースホルダ名とその値のマップ
    private final Map<String, String> params;

    /**
     * コンストラクタ。指定されたパラメータマップを使用してインスタンスを生成します。
     *
     * @param params プレースホルダ名と値のマッピング
     * @throws IllegalArgumentException パラメータがnullの場合に発生
     */
    public PlaceholderValueMapProvider(Map<String, String> params) {
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null");
        }
        this.params = params;
    }

    /**
     * 指定されたプレースホルダ名に対応する値を返します。
     *
     * @param name プレースホルダ名
     * @return 存在する場合はOptionalで包まれた値、存在しない場合はOptional.empty()
     */
    @Override
    public Optional<String> get(String name) {
        return Optional.ofNullable(params.get(name));
    }

    /**
     * プレースホルダ名と値のマッピングを返します。
     *
     * @return プレースホルダ名と値のマップ
     */
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
