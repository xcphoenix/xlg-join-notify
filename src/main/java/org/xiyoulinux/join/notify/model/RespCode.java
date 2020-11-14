package org.xiyoulinux.join.notify.model;

import lombok.Getter;

/**
 * NOTE: 考虑到性能，暂时不用异常来处理逻辑（虽然全局异常处理很爽）
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/13 下午8:39
 */
@Getter
public enum RespCode {

    SUCCESS(true, "20000", ""),
    FAILURE(false, "50000", "服务异常"),
    /*
     * 业务相关
     */
    SENDER_UNSETTLED(false, "30001", "发送者有处理中的面试邀请，无法删除"),
    ALL_UNSETTLED(false, "30002", "存在处理中的面试邀请，无法重置")
    ;

    RespCode(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private final boolean success;
    private final String code;
    private final String message;

}
