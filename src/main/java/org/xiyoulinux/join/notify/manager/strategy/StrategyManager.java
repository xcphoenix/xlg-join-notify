package org.xiyoulinux.join.notify.manager.strategy;

import org.springframework.stereotype.Component;
import org.xiyoulinux.join.notify.model.strategy.StrategyType;
import org.xiyoulinux.join.notify.utils.SpringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据 {@link StrategyType} 获取到对应的策略分发器
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:53
 */
@Component
public class StrategyManager {

    private Map<StrategyType, DispatchStrategy> type2Strategy;

    public DispatchStrategy getStrategy(StrategyType type) {
        if (type2Strategy == null) {
            Map<String, DispatchStrategy> strategyMap = SpringUtils.getApplicationContext()
                    .getBeansOfType(DispatchStrategy.class);
            type2Strategy = new HashMap<>(strategyMap.size());
            strategyMap.values().forEach(dispatchStrategy -> type2Strategy.put(dispatchStrategy.getType(), dispatchStrategy));
        }
        if (type2Strategy.containsKey(type)) {
            return type2Strategy.get(type);
        }
        return null;
    }

}
