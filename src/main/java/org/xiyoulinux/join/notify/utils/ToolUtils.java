package org.xiyoulinux.join.notify.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/7 下午3:27
 */
public class ToolUtils {

    public static final int DEFAULT_PAGE_SIZE = 200;

    /*
     * COLLECTION
     */

    @SuppressWarnings("rawtypes")
    public static boolean isEmptyCollection(Collection... collections) {
        if (collections == null || collections.length == 0) {
            return true;
        }
        for (Collection collection : collections) {
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(collection)) {
                return true;
            }
        }
        return false;
    }

    /*
     * STRING
     */

    public static Map<String, String> parseMap(String data) {
        return parseMap(data, ";", ":");
    }

    /**
     * 字符串解析为 Map，若参数有一个为空则返回空Map
     *
     * @param data          需要解析的字符串
     * @param itemDelimiter 每项之间的分隔符
     * @param kvDelimiter   key value 分隔符
     * @return 解析后的 Map，有序
     */
    public static Map<String, String> parseMap(String data, String itemDelimiter, String kvDelimiter) {
        if (StringUtils.isAnyEmpty(data, itemDelimiter, kvDelimiter)) {
            return Collections.emptyMap();
        }
        StringTokenizer tokenizer = new StringTokenizer(data, itemDelimiter);
        Map<String, String> map = new LinkedHashMap<>();
        while (tokenizer.hasMoreTokens()) {
            String item = tokenizer.nextToken();
            int valIndex = item.indexOf(kvDelimiter);
            if (valIndex < 0) {
                map.put(item, null);
            } else {
                map.put(item.substring(0, valIndex), item.substring(valIndex + 1));
            }
        }
        return map;
    }

    /*
     * PAGE
     */

    /**
     * 分页操作
     *
     * <b>以返回值来判断分页是否结束</b>
     *
     * @param startNum 开始页数，从 1 开始
     * @param pageEndNum 结束页数，若为非正整数则获取全部
     * @param pageSize 分页大小
     * @param operation 操作，接收当前页数、分页大小为参数
     * @param <T> 分页返回的类型
     * @return 汇总的数据
     * @throws IllegalArgumentException startNum < 0 或 pageSize <= 0
     */
    public static  <T> List<T> pageOperation(int startNum, int pageEndNum, int pageSize,
                                     BiFunction<Integer, Integer, List<T>> operation) {
        argAssert(startNum, num -> num > 0);
        argAssert(pageSize, num -> num > 0);
        if (pageEndNum <= 0) {
            pageEndNum = Integer.MAX_VALUE;
        }

        List<T> result = new ArrayList<>();
        for (int i = startNum; i <= pageEndNum; i++) {
            List<T> pageResult = operation.apply(i, pageSize);
            result.addAll(Optional.ofNullable(pageResult).orElse(Collections.emptyList()));
            if (CollectionUtils.isEmpty(pageResult) || pageResult.size() < pageSize) {
                break;
            }
        }

        return result;
    }

    public static  <T> List<T> pageOperation(int pageSize, BiFunction<Integer, Integer, List<T>> operation) {
        return pageOperation(1, -1, pageSize, operation);
    }

    public static  <T> List<T> pageOperation(BiFunction<Integer, Integer, List<T>> operation) {
        return pageOperation(DEFAULT_PAGE_SIZE, operation);
    }

    /*
     * ASSERT
     */

    public static  <T> void argAssert(T obj, Predicate<T> predicate) {
        argAssert(obj, predicate, obj + " can't meet the condition ");
    }

    public static  <T> void argAssert(T obj, Predicate<T> predicate, String throwMessage) {
        if (!predicate.test(obj)) {
            throw new IllegalArgumentException(throwMessage);
        }
    }


}
