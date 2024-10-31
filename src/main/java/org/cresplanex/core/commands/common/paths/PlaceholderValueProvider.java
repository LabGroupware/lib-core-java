package org.cresplanex.core.commands.common.paths;

import java.util.Map;
import java.util.Optional;

/**
 * プレースホルダに値を提供するインターフェース。
 */
public interface PlaceholderValueProvider {

    /**
     * 指定されたプレースホルダ名に対応する値を返します。
     *
     * @param name プレースホルダ名
     * @return 値が存在する場合はOptionalで包まれた値、存在しない場合はOptional.empty()
     */
    Optional<String> get(String name);

    /**
     * プレースホルダとその値のマッピングを返します。
     *
     * @return プレースホルダと値のマップ
     */
    Map<String, String> getParams();
}
