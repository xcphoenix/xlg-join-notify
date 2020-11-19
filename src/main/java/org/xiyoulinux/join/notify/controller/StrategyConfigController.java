package org.xiyoulinux.join.notify.controller;

import org.springframework.web.bind.annotation.*;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.constant.ConfigKeyConst;
import org.xiyoulinux.join.notify.model.dto.result.RespCode;
import org.xiyoulinux.join.notify.model.dto.result.Result;
import org.xiyoulinux.join.notify.model.vo.StrategyConfigVO;
import org.xiyoulinux.join.notify.service.ConfigService;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/14 下午9:36
 */
@RestController
@RequestMapping("/config")
public class StrategyConfigController {

    private final ConfigService configService;

    public StrategyConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 获取配置
     */
    @GetMapping("/strategy")
    public Result<StrategyConfigVO> getStrategyConfig() {
        StrategyConfig config = configService.getStrategyCfg(false);
        if (config == null) {
            return Result.<StrategyConfigVO>builder(null).fromResp(RespCode.CONFIG_NOT_SET);
        }
        StrategyConfigVO configVO = StrategyConfigVO.build(config);
        return Result.builder(configVO).success();
    }

    /**
     * 更新配置
     */
    @PostMapping("/strategy")
    public Result<Void> updateStrategyConfig(@RequestBody StrategyConfigVO config) {
        if (config == null) {
            return Result.<Void>builder().fromResp(RespCode.CONFIG_INVALID);
        }
        StrategyConfig strategyConfig = config.parse();
        if (!strategyConfig.checkValid()) {
            return Result.<Void>builder().fromResp(RespCode.CONFIG_INVALID);
        }
        configService.updateConfig(ConfigKeyConst.STRATEGY_KEY, strategyConfig);
        return Result.<Void>builder().success(null);
    }

}
