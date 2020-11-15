package org.xiyoulinux.join.notify.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xiyoulinux.join.notify.mapper.ConfigMapper;
import org.xiyoulinux.join.notify.model.po.Config;
import org.xiyoulinux.join.notify.service.ConfigService;

import java.util.Optional;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:56
 */
@Log4j2
@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigMapper configMapper;

    public ConfigServiceImpl(ConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    public String getConfig(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return Optional.ofNullable(
                // very amazing 一系列反射的牛皮操作获取字段名
                configMapper.selectOne(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, key))
        ).map(Config::getConfigValue).orElse(null);
    }

    @Override
    public <T> T getJsonSerialConfig(@NonNull String key, @NonNull Class<T> clazz) {
        return Optional.ofNullable(this.getConfig(key))
                .map(val -> {
                    try {
                        return new ObjectMapper().readValue(val, clazz);
                    } catch (JsonProcessingException e) {
                        log.error("parse config [" + key + "] error", e);
                    }
                    return null;
                })
                .orElse(null);
    }

    @Override
    public boolean updateStrConfig(@NonNull String key, @NonNull String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Config config = new Config();
        config.setConfigKey(key);
        config.setConfigValue(value);
        // TODO 返回值意义
        if (configMapper.selectCount(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, key)) > 0) {
            return configMapper.update(config, Wrappers.<Config>lambdaUpdate().eq(Config::getConfigKey, key)) == 1;
        } else {
            return configMapper.insert(config) == 1;
        }
    }

    @Override
    public <T> boolean updateConfig(@NonNull String key, @NonNull T value) {
        String serialJson;
        try {
            serialJson = new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("serial config failed, key [" + key + "], value [" + value + "]", e);
            return false;
        }
        return this.updateStrConfig(key, serialJson);
    }

}
