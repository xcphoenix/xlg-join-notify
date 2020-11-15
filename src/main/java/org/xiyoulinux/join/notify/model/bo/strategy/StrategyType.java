package org.xiyoulinux.join.notify.model.bo.strategy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:16
 */
@Getter
public enum StrategyType {

    /**
     * 随机策略使用学号取余，
     * 高级策略根据年级、专业等排序
     */
    RANDOM(1, "随机策略"),
    TIME(2, "报名顺序"),
    MATCH(3, "匹配策略");

    @EnumValue
    private final int type;
    private final String desc;

    StrategyType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static StrategyType fromType(Integer type) {
        if (type != null) {
            for (StrategyType strategyType : StrategyType.values()) {
                if (strategyType.getType() == type) {
                    return strategyType;
                }
            }
        }
        throw new IllegalArgumentException("invalid strategy type : " + type);
    }

}
