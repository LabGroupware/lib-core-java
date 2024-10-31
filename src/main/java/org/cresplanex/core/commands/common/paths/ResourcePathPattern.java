package org.cresplanex.core.commands.common.paths;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.stream.Collectors.toList;

/**
 * リソースのパスパターンを表現するクラス。パス内のプレースホルダに対応する値の取得および置換を行います。
 */
public class ResourcePathPattern {

    // パスパターンを分割したセグメントの配列
    private final String[] splits;

    /**
     * コンストラクタ。指定したパスパターンを"/"で分割してセグメントに変換します。
     *
     * @param pathPattern "/"で始まるパスパターン
     * @throws IllegalArgumentException パスパターンが"/"で始まらない場合に発生
     */
    public ResourcePathPattern(String pathPattern) {
        if (!pathPattern.startsWith("/")) {
            throw new IllegalArgumentException("Should start with / " + pathPattern);
        }
        this.splits = splitPath(pathPattern);
    }

    /**
     * パスを分割してセグメント配列を作成します。
     *
     * @param path 分割するパス文字列
     * @return 分割されたセグメント配列
     */
    private static String[] splitPath(String path) {
        return path.split("/");
    }

    /**
     * 指定されたパスパターンを解析してResourcePathPatternを生成します。
     *
     * @param pathPattern パスパターン文字列
     * @return 新しいResourcePathPatternインスタンス
     */
    public static ResourcePathPattern parse(String pathPattern) {
        return new ResourcePathPattern(pathPattern);
    }

    /**
     * パターンの長さ（セグメント数）を返します。
     *
     * @return パターンのセグメント数
     */
    public int length() {
        return splits.length;
    }

    /**
     * 指定されたResourcePathがこのパターンに一致するかどうかを確認します。
     *
     * @param mr ResourcePathオブジェクト
     * @return パターンに一致すればtrue、一致しなければfalse
     */
    public boolean isSatisfiedBy(ResourcePath mr) {
        if (splits.length != mr.splits.length) {
            return false;
        }
        for (int i = 0; i < mr.splits.length; i++) {
            if (!pathSegmentMatches(splits[i], mr.splits[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * パターンセグメントがプレースホルダか、パスセグメントと一致するかを判定します。
     *
     * @param patternSegment パターンのセグメント
     * @param pathSegment 比較するパスのセグメント
     * @return プレースホルダまたは一致する場合にtrue、その他の場合はfalse
     */
    private boolean pathSegmentMatches(String patternSegment, String pathSegment) {
        return isPlaceholder(patternSegment) || patternSegment.equals(pathSegment);
    }

    /**
     * パターンセグメントがプレースホルダかどうかを判定します。
     *
     * @param patternSegment 判定するパターンセグメント
     * @return プレースホルダであればtrue、それ以外はfalse
     */
    private boolean isPlaceholder(String patternSegment) {
        return patternSegment.startsWith("{");
    }

    /**
     * プレースホルダに対応するパス変数の値を取得します。
     *
     * /aaa/{bbb}/ccc のようなパスパターンに対して、ResourcePathオブジェクトが /aaa/123/ccc の場合、
     * プレースホルダ名 bbb と値 123 のマッピングを返します。
     *
     * @param mr ResourcePathオブジェクト
     * @return プレースホルダ名と値のマッピング
     */
    public Map<String, String> getPathVariableValues(ResourcePath mr) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < mr.splits.length; i++) {
            String name = splits[i];
            if (isPlaceholder(name)) {
                result.put(placeholderName(name), mr.splits[i]);
            }
        }
        return result;
    }

    /**
     * プレースホルダ名から波括弧を除いた名前を取得します。
     *
     * @param name プレースホルダ名
     * @return 波括弧を除いた名前
     */
    private String placeholderName(String name) {
        return name.substring(1, name.length() - 1);
    }

    /**
     * プレースホルダの値を提供するプロバイダーを使用してプレースホルダを置換したResourcePathを返します。
     *
     * /aaa/{bbb}/ccc のようなパスパターンがあり, placeholderValueProvider.get("bbb") が
     * Optional.of("123") を返す場合, /aaa/123/ccc というResourcePathオブジェクトを返す. ただし,
     * placeholderValueProvider.get("bbb") が Optional.empty() を返す場合は,
     * RuntimeException が発生する.
     *
     * @param placeholderValueProvider プレースホルダの値を提供するプロバイダー
     * @return プレースホルダを置換した新しいResourcePathインスタンス
     * @throws RuntimeException プレースホルダに対応する値が存在しない場合
     */
    public ResourcePath replacePlaceholders(PlaceholderValueProvider placeholderValueProvider) {
        return new ResourcePath(Arrays.stream(splits).map(s -> isPlaceholder(s) ? placeholderValueProvider.get(placeholderName(s)).orElseGet(() -> {
            throw new RuntimeException("Placeholder not found: " + placeholderName(s) + " in " + s + ", params=" + placeholderValueProvider.getParams());
        }) : s).collect(toList()).toArray(new String[splits.length]));
    }

    /**
     * 指定されたパラメータを使用してプレースホルダを置換したResourcePathを返します。
     *
     * /aaa/{bbb}/ccc/{d} のようなパスパターンがあり, pathParams が ["123", "456"] の場合, /aaa/123/ccc/456 という
     * ResourcePathオブジェクトを返す. ただし, pathParams が ["123"] の場合は, RuntimeException
     * が発生する.
     *
     * @param pathParams プレースホルダに対応する値の配列
     * @return プレースホルダを置換した新しいResourcePathインスタンス
     * @throws RuntimeException プレースホルダに対応する値が存在しない場合
     */
    public ResourcePath replacePlaceholders(Object[] pathParams) {
        AtomicInteger idx = new AtomicInteger(0);
        return new ResourcePath(Arrays.stream(splits).map(s -> isPlaceholder(s) ? getPlaceholderValue(pathParams, idx.getAndIncrement()).orElseGet(() -> {
            throw new RuntimeException("Placeholder not found: " + placeholderName(s) + " in " + s + ", params=" + Arrays.asList(pathParams));
        }) : s).collect(toList()).toArray(new String[splits.length]));
    }

    /**
     * 指定されたインデックスのパラメータからプレースホルダの値を取得します。
     *
     * @param pathParams パラメータ配列
     * @param idx インデックス
     * @return インデックスに対応する値があればその値、なければ空のOptional
     */
    private Optional<String> getPlaceholderValue(Object[] pathParams, int idx) {
        return idx < pathParams.length ? Optional.of(pathParams[idx].toString()) : Optional.empty();
    }

}
