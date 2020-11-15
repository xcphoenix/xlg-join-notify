package org.xiyoulinux.join.notify.model.vo;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.xiyoulinux.join.notify.model.bo.ProcessStatus;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyType;

import java.util.Arrays;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午1:09
 */
@Data
public class StrategyConfigVO {

    private Integer processId;
    private String processDesc;
    private List<String> timeSegments;
    private Integer strategy;
    private String strategyDesc;
    private String strategyData;
    private List<String> blackRegexes;

    public StrategyConfig parse() {
        StrategyConfig config = new StrategyConfig();
        BeanUtils.copyProperties(this, config);
        if (CollectionUtils.isNotEmpty(blackRegexes)) {
            config.applyBlackRegexes(blackRegexes.toArray(new String[0]));
        }
        config.setStrategyType(StrategyType.fromType(strategy));
        return config;
    }

    public static StrategyConfigVO build(@NonNull StrategyConfig config) {
        StrategyConfigVO configVO = new StrategyConfigVO();
        BeanUtils.copyProperties(config, configVO);
        ProcessStatus status = ProcessStatus.fromStatus(config.getProcessId());
        configVO.setProcessDesc(status.getDesc());
        if (StringUtils.isNotBlank(config.getBlackRegex())) {
            configVO.setBlackRegexes(Arrays.asList(config.parseBlackRegexes()));
        }
        configVO.setStrategy(config.getStrategyType().getType());
        configVO.setStrategyDesc(config.getStrategyType().getDesc());
        return configVO;
    }

}
