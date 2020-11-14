package org.xiyoulinux.join.notify.model.strategy;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午8:28
 */
@Data
public class StrategyConfig {

    /**
     * 黑名单正则表达式分隔符
     */
    private static final char REGEX_DELIMITER = '\b';

    private Integer processId;

    /**
     * 逗号分割
     */
    private List<String> timeSegments;

    private StrategyType strategyType;

    /**
     * 策略数据补充
     *
     * @see org.xiyoulinux.join.notify.manager.strategy.impl.MatchDispatchStrategyBase
     */
    private String strategyData;

    /**
     * 黑名单正则
     */
    private String blackRegex;

    public void applyBlackRegexes(String ... regexes) {
        this.setBlackRegex(StringUtils.join(regexes, String.valueOf(REGEX_DELIMITER)));
    }

    public String[] parseBlackRegexes() {
        return StringUtils.split(this.getBlackRegex(), REGEX_DELIMITER);
    }

}
