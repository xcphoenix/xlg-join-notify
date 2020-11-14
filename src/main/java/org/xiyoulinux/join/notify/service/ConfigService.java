package org.xiyoulinux.join.notify.service;

import com.sun.istack.internal.NotNull;
import org.xiyoulinux.join.notify.model.constant.ConfigKeyConst;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.Objects;

/**
 * 配置管理
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:50
 */
public interface ConfigService {

    String getConfig(@NotNull String key);

    <T> T getJsonSerialConfig(@NotNull String key, @NotNull Class<T> clazz);

    boolean updateStrConfig(@NotNull String key, @NotNull String value);

    <T> boolean updateConfig(@NotNull String key, @NotNull T value);

    /*
     * for special key
     */

     default StrategyConfig getStrategyCfg(boolean required) {
        StrategyConfig config = getJsonSerialConfig(ConfigKeyConst.STRATEGY_KEY, StrategyConfig.class);
        if (required) {
            ToolUtils.argAssert(config, Objects::nonNull, "strategy config can't be null");
        }
        return config;
    }

}
