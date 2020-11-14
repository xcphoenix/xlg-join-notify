package org.xiyoulinux.join.notify.utils;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author      xuanc
 * @date        2020/11/7 下午4:12
 * @version     1.0
 */
@Getter
public class OrderPriorityWrapper<T> {

    /**
     * 排序项目的优先级
     */
    private final int level;
    /**
     * 排序的数据
     */
    T data;

    public OrderPriorityWrapper(int level, T data) {
        this.level = level;
        this.data = data;
    }

    public OrderPriorityWrapper(T data) {
        this(0, data);
    }

    /**
     * 排序
     *
     * @param priorityDataList 包装类型数据
     * @param comparator 兜底比较器
     * @param <E> 被包装的数据类型
     * @return 排序后的列表
     */
    public static <E> List<OrderPriorityWrapper<E>> order(List<OrderPriorityWrapper<E>> priorityDataList,
                                                          @NonNull Comparator<E> comparator) {
        if (CollectionUtils.isEmpty(priorityDataList)) {
            return Collections.emptyList();
        }

        // 静态方法还是尽量避免副作用
        List<OrderPriorityWrapper<E>> orderedDataList = new ArrayList<>(priorityDataList);
        orderedDataList.sort((o1, o2) -> {
            if (o1.level != o2.level) {
                return o1.level - o2.level;
            }
            return comparator.compare(o1.getData(), o2.getData());
        });
        return orderedDataList;
    }

}
