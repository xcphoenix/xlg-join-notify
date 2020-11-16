package org.xiyoulinux.join.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import lombok.NonNull;
import org.xiyoulinux.join.notify.model.bo.InvitationDetail;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午7:55
 */
public interface InvitationService extends IService<Invitation> {

    boolean batchInsert(List<Invitation> invitationList);

    /**
     * 获取邀请详情
     *
     * @param condition 不能为空，应当指定流程 id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return 邀请详情，以及报名记录
     */
    PageResult<InvitationDetail> getInvitation(@NotNull Invitation condition, int pageNum, int pageSize);

    /**
     * 获取未分配邀请的报名记录
     *
     * @param processId 流程id
     * @return 未处理的列表
     */
    PageResult<Join> getUnDispatchJoin(@NotNull Integer processId, int pageNum, int pageSize);

    boolean updateById(@NotNull Long id, @NotNull Invitation newInvitation);

    boolean updateStatus(@NotNull Integer processId,
                         @NotNull InviteStatus status,
                         @NotNull InviteStatus newStatus);

    /**
     * 是否存在未处理的邀请
     *
     * @param senderId 通知者id，为空时不对通知者进行限制
     * @param processId 流程id，不能为空
     * @return 是否处理成功
     */
    boolean hasUnsettled(@Nullable Long senderId, @NotNull Integer processId);

    int countByStatus(@NotNull Integer processId, @NotNull InviteStatus status);

    List<String> groupByTime(@NonNull Integer processId);

    boolean clean(@NotNull Invitation condition);

}
