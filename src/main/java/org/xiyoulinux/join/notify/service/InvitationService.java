package org.xiyoulinux.join.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.xiyoulinux.join.notify.model.InviteStatus;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Join;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午7:55
 */
public interface InvitationService extends IService<Invitation> {

    boolean batchInsert(List<Invitation> invitationList);

    List<Invitation> getInvitation(@NotNull Invitation condition);

    boolean updateById(@NotNull Long id, @NotNull Invitation newInvitation);

    boolean updateStatus(@NotNull Integer processId, @NotNull InviteStatus status, @NotNull InviteStatus newStatus);

    /**
     * 是否存在未处理的邀请
     *
     * @param senderId 通知者id，为空时不对通知者进行限制
     * @param processId 流程id，不能为空
     * @return 是否处理成功
     */
    boolean hasUnsettled(@Nullable Long senderId, @NotNull Integer processId);

    int countByStatus(@NotNull Integer processId, @NotNull InviteStatus status);

    boolean clean(@NotNull Invitation condition);

    /**
     * 获取未分配邀请的报名记录
     *
     * @param processId 流程id
     * @return 未处理的列表
     */
    List<Join> getUnDispatchJoin(@NotNull Integer processId);

}
