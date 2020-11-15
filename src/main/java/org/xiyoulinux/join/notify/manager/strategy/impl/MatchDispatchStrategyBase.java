package org.xiyoulinux.join.notify.manager.strategy.impl;

import com.sun.istack.internal.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xiyoulinux.join.notify.manager.strategy.BaseOrderDispatchStrategy;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyType;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.utils.OrderPriorityWrapper;
import org.xiyoulinux.join.notify.utils.SnoUtils;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 默认字典序，年级优先和专业优先可叠加
 * - 1.字典序（升降序没什么意义，直接升序）
 * - 2.指定年级优先/最后
 * - 3.指定专业优先/最后
 *
 * <br /><br />
 * <p>当前优先级仅支持覆盖，即取最高或最低的优先级，不支持优先级的叠加</p>
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午10:20
 */
@Component
public class MatchDispatchStrategyBase extends BaseOrderDispatchStrategy {

    /**
     * 最大排序数量
     */
    public static final int MAX_ORDER_NUM = 25;

    @Override
    public List<Join> order(List<Join> joinList, StrategyConfig strategyConfig) {
        // 拼音排序 - 忽略音调
        final HanyuPinyinOutputFormat outputF = new HanyuPinyinOutputFormat();
        outputF.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputF.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        List<OrderPriorityWrapper<Join>> joinWrappedList = parseAttachData(strategyConfig.getStrategyData(), joinList);
        List<OrderPriorityWrapper<Join>> orderedJoinList = OrderPriorityWrapper.order(joinWrappedList,
                (o1, o2) -> {
                    String o1AdminClass;
                    String o2AdminClass;
                    try {
                        o1AdminClass = PinyinHelper.toHanYuPinyinString(o1.getAdminClass());
                        o2AdminClass = PinyinHelper.toHanYuPinyinString(o2.getAdminClass());
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        badHanyuPinyinOutputFormatCombination.printStackTrace();
                        o1AdminClass = o1.getAdminClass();
                        o2AdminClass = o2.getAdminClass();
                    }
                    return o1AdminClass.compareTo(o2AdminClass);
                });

        return orderedJoinList.stream().map(OrderPriorityWrapper::getData).collect(Collectors.toList());
    }

    @Override
    public StrategyType getType() {
        return StrategyType.MATCH;
    }

    @Data
    @Accessors(chain = true)
    private static class ItemContext {
        private String item;
        private int level;
    }

    /**
     * 规则匹配类型
     */
    @Getter(AccessLevel.PRIVATE)
    private enum RuleType {
        /**
         * 专业规则
         */
        MAJOR("M", join -> SnoUtils.getMajor(join.getAdminClass())),
        /**
         * 年级规则
         */
        GRADE("G", join -> SnoUtils.getGradeStr(join.getStudentNo()));

        private final String type;

        private final Function<Join, String> matchItem;

        ItemContext getMatchCtx(List<ItemContext> contexts, Join join) {
            for (ItemContext context : contexts) {
                if (context.getItem().equals(this.getMatchItem().apply(join))) {
                    return context;
                }
            }
            return null;
        }

        RuleType(String type, Function<Join, String> matchItem) {
            this.type = type;
            this.matchItem = matchItem;
        }

        static RuleType fromType(String type) {
            if (StringUtils.isBlank(type)) {
                return null;
            }
            for (RuleType ruleType : RuleType.values()) {
                if (ruleType.getType().equals(type)) {
                    return ruleType;
                }
            }
            return null;
        }
    }

    /**
     * 解析附加数据
     *
     * @param joinList join 列表
     * @return 包装后的 join 列表
     */
    public List<OrderPriorityWrapper<Join>> parseAttachData(String strategyData, @Nullable List<Join> joinList) {
        if (CollectionUtils.isEmpty(joinList)) {
            return Collections.emptyList();
        }

        // 配置解析
        Map<String, String> map = ToolUtils.parseMap(strategyData);
        List<OrderPriorityWrapper<Join>> orderPriorityWrapperList = new ArrayList<>(map.size());
        // lambda 表达式限制，需要是 effectively final 或者 final，此处使用数组来控制变量
        final int[] level = {map.size()};
        final String highFlag = "1";
        final String prDelimiter = "|";
        final String itemDelimiter = ",";

        Map<RuleType, List<ItemContext>> type2Ctx = new HashMap<>(map.size());

        // 条目对应的优先级处理
        map.forEach((k, v) -> {
            RuleType type = RuleType.fromType(k);
            // 过滤无效规则
            if (type == null || StringUtils.isBlank(v)) {
                return;
            }

            final String[] items = StringUtils.split(v, itemDelimiter);
            List<ItemContext> contextList = new ArrayList<>(items.length);
            for (int i = 0; i < items.length; i++) {
                final String item = items[i];

                int priorityIndex = item.indexOf(prDelimiter);
                ItemContext itemCtx = new ItemContext();

                itemCtx.setLevel(level[0] * MAX_ORDER_NUM + item.length() - i);
                if (priorityIndex < 0) {
                    itemCtx.setItem(item);
                    // 默认为先放置匹配的报名记录，在升序排列中优先级应该较低，故设置为负数
                    itemCtx.setLevel(itemCtx.getLevel() * -1);
                } else {
                    itemCtx.setItem(item.substring(0, priorityIndex));
                    // 显示设置先匹配
                    if (highFlag.equals(v.substring(priorityIndex + 1))) {
                        itemCtx.setLevel(itemCtx.getLevel() * -1);
                    }
                }
                contextList.add(itemCtx);
            }
            type2Ctx.put(type, contextList);

            level[0]--;
        });

        // 优先级匹配注入
        joinList.forEach(join -> {
            AtomicInteger joinLevel = new AtomicInteger();
            // 遍历每个规则，以距原点最远的优先级为准
            type2Ctx.keySet().forEach(ruleType -> {
                List<ItemContext> matchItems = type2Ctx.get(ruleType);
                ItemContext matchedCtx = ruleType.getMatchCtx(matchItems, join);
                if (matchedCtx != null && Math.abs(joinLevel.get()) < Math.abs(matchedCtx.getLevel())) {
                    joinLevel.set(matchedCtx.getLevel());
                }
            });
            orderPriorityWrapperList.add(new OrderPriorityWrapper<>(joinLevel.get(), join));
        });

        return orderPriorityWrapperList;
    }

}
