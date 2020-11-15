package org.xiyoulinux.join.notify.model.vo;

import lombok.Data;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyType;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午1:09
 */
@Data
public class StrategyConfigVO {

    private Integer processId;
    private List<String> timeSegments;
    private StrategyType strategyType;
    private String strategyData;
    private List<String> blackRegexes;

}
