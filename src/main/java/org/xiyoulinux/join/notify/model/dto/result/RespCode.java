package org.xiyoulinux.join.notify.model.dto.result;

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

    SUCCESS(true, "20000", "success"),
    FAILURE(false, "50000", "服务异常"),
    /*
     * 业务相关
     */
    SENDER_UNSETTLED(false, "30001", "发送者有处理中的面试邀请，无法删除"),
    ALL_UNSETTLED(false, "30002", "存在处理中的面试邀请，无法重置"),
    CONFIG_NOT_SET(false, "30003", "找不到配置"),
    CONFIG_INVALID(false, "30004", "无效的配置"),
    DISPATCH_NOT_FOUND(false, "30005", "没有待分发的邀请"),
    SENDER_OR_JOIN_NOT_FOUND(false, "30006", "找不到有效的发送者数据或待邀请报名记录"),
    SENDER_EXISTED(false, "30007", "已经有存在的发送者"),
    SENDER_NOT_EXIST(false, "30008", "发送者不存在");

    RespCode(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private final boolean success;
    private final String code;
    private final String message;

    public static final String ARG_CODE = "40000";

}
