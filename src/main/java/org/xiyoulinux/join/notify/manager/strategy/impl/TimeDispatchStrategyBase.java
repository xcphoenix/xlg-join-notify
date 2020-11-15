package org.xiyoulinux.join.notify.manager.strategy.impl;

import org.springframework.stereotype.Component;
import org.xiyoulinux.join.notify.manager.strategy.BaseOrderDispatchStrategy;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyType;
import org.xiyoulinux.join.notify.model.po.Join;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午10:14
 */
@Component
public class TimeDispatchStrategyBase extends BaseOrderDispatchStrategy {

    @Override
    public List<Join> order(List<Join> joinList, StrategyConfig strategyConfig) {
        List<Join> sortedJoinList = new ArrayList<>(joinList);
        // 升序排列
        sortedJoinList.sort(Comparator.comparing(Join::getId).reversed());
        return sortedJoinList;
    }

    @Override
    public StrategyType getType() {
        return StrategyType.TIME;
    }

}
