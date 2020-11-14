package org.xiyoulinux.join.notify.model.dao;

import lombok.Data;

/**
 * 配置
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午4:55
 */
@Data
public class Config {

    private Long id;
    private String configKey;
    private String configValue;

}
