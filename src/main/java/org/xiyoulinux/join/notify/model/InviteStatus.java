package org.xiyoulinux.join.notify.model;

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
    INIT(-1),
    /**
     * 未通知，初始状态
     */
    NOT_NOTIFY(0),
    /**
     * 待确认
     */
    TO_BE_CONFIRM(1),
    /**
     * 有异议
     */
    DISSENT(2),
    /**
     * 已接受
     */
    APPLY(3),
    /**
     * 已拒绝
     */
    REFUSED(4);

    @EnumValue
    private final int type;

    InviteStatus(int type) {
        this.type = type;
    }

    public static List<InviteStatus> unDealStatus() {
        return Arrays.asList(
                InviteStatus.INIT,
                InviteStatus.NOT_NOTIFY
        );
    }

}
