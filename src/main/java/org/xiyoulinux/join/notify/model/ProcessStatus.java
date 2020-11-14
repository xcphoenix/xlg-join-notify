package org.xiyoulinux.join.notify.model;

import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/11 下午1:47
 */
@Getter
public enum ProcessStatus {
    /**
     * 面试轮次，B轮为0（貌似由于历史因素）
     */
    ROUND_A(1),
    ROUND_B(0),
    ROUND_C(2),
    ROUND_D(3);

    private final int status;

    ProcessStatus(int status) {
        this.status = status;
    }

}
