package org.xiyoulinux.join.notify.model.bo;

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
    ROUND_A(1, "A轮面试"),
    ROUND_B(0, "B轮面试"),
    ROUND_C(2, "C轮面试"),
    ROUND_D(3, "D轮面试");

    private final int status;
    private final String desc;

    ProcessStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ProcessStatus fromStatus(Integer status) {
        if (status != null) {
            for (ProcessStatus processStatus : ProcessStatus.values()) {
                if (processStatus.getStatus() == status) {
                    return processStatus;
                }
            }
        }
        throw new IllegalArgumentException("invalid process status: " + status);
    }

}
