package org.xiyoulinux.join.notify.manager.strategy.impl;

import org.springframework.stereotype.Component;
import org.xiyoulinux.join.notify.manager.strategy.BaseOrderDispatchStrategy;
import org.xiyoulinux.join.notify.model.dao.Join;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.strategy.StrategyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午8:15
 */
@Component
public class RandomDispatchStrategyBase extends BaseOrderDispatchStrategy {

    @Override
    public List<Join> order(List<Join> joinList, StrategyConfig strategyConfig) {
        List<Join> randomJoinList = new ArrayList<>(joinList);
        Collections.shuffle(randomJoinList);
        return randomJoinList;
    }

    @Override
    public StrategyType getType() {
        return StrategyType.RANDOM;
    }

}
