package org.xiyoulinux.join.notify.model.bo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:27
 */
@Getter
public enum InviteStatus {

    /**
     * 初始状态
     */
    INIT(-1, "初始状态"),
    /**
     * 未通知，初始状态
     */
    NOT_NOTIFY(0, "未通知"),
    /**
     * 待确认
     */
    TO_BE_CONFIRM(1, "待确认"),
    /**
     * 有异议
     */
    DISSENT(2, "有异议"),
    /**
     * 已接受
     */
    APPLY(3, "已接受"),
    /**
     * 已拒绝
     */
    REFUSED(4, "已拒绝");

    @EnumValue
    private final int type;

    private final String desc;

    InviteStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static List<InviteStatus> unDealStatus() {
        return Arrays.asList(
                InviteStatus.INIT,
                InviteStatus.NOT_NOTIFY
        );
    }

}
