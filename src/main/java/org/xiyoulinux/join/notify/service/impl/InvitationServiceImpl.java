package org.xiyoulinux.join.notify.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.xiyoulinux.join.notify.mapper.InvitationMapper;
import org.xiyoulinux.join.notify.model.bo.InvitationDetail;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.model.po.Sender;
import org.xiyoulinux.join.notify.service.InvitationService;
import org.xiyoulinux.join.notify.service.JoinService;
import org.xiyoulinux.join.notify.service.SenderService;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午8:13
 */
@Log4j2
@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

    private final InvitationMapper invitationMapper;

    private final JoinService joinService;

    @Resource
    private SenderService senderService;

    public InvitationServiceImpl(InvitationMapper invitationMapper, JoinService joinService) {
        this.invitationMapper = invitationMapper;
        this.joinService = joinService;
    }

    @Override
    public boolean batchInsert(List<Invitation> invitationList) {
        if (CollectionUtils.isEmpty(invitationList)) {
            return false;
        }
        return saveBatch(invitationList, ToolUtils.DEFAULT_PAGE_SIZE);
    }

    @Override
    public PageResult<InvitationDetail> getInvitation(@NonNull Invitation condition, int pageNum, int pageSize) {
        validPageParam(pageNum, pageSize);

        Page<Invitation> page = page(new Page<>(pageNum, pageSize), Wrappers.lambdaQuery(condition));
        final List<Invitation> invitationList = page.getRecords();

        final List<Integer> joinIds = invitationList.stream().map(Invitation::getJoinId).collect(Collectors.toList());
        final List<Join> joins = joinService.listByIds(joinIds);
        Map<Integer, Join> id2Join = joins.stream().collect(Collectors.toMap(Join::getId, join -> join));

        final List<Long> senderId = invitationList.stream().map(Invitation::getSenderId).collect(Collectors.toList());
        final List<Sender> senders = senderService.listByIds(senderId);
        Map<Long, Sender> id2Sender = senders.stream().collect(Collectors.toMap(Sender::getId, sender -> sender));

        List<InvitationDetail> invitationDetails = invitationList.stream()
                .map(InvitationDetail::new)
                .peek(inDetail -> inDetail.setJoin(id2Join.get(inDetail.getInvitation().getJoinId())))
                .peek(inDetail -> inDetail.setSender(id2Sender.get(inDetail.getInvitation().getSenderId())))
                .collect(Collectors.toList());

        return PageResult.pageBuilder(invitationDetails)
                .success()
                .setTotal(page.getTotal())
                .setCurPage(pageNum)
                .setAllPage((int) Math.ceil(1.0 * page.getTotal() / pageSize))
                .setHasNext(page.hasNext());
    }

    @Override
    public PageResult<Join> getUnDispatchJoin(@NonNull Integer processId, int pageNum, int pageSize) {
        validPageParam(pageNum, pageSize);

        final long pageStart = (pageNum - 1) * pageSize;
        final long total = invitationMapper.countUnDispatchJoin(processId);
        if (total <= 0) {
            return PageResult.<Join>pageBuilder().success();
        }
        return PageResult.pageBuilder(invitationMapper.getUnDispatchJoin(processId, pageStart, pageSize))
                .success()
                .setCurPage(pageNum)
                .setTotal(total)
                .setAllPage((int) Math.ceil(1.0 * total / pageSize));
    }

    @Override
    public boolean updateById(@NonNull Long id, @NonNull Invitation newInvitation) {
        // NOTE: mybatis-plus 的 IService 写着有点混...
        return super.updateById(newInvitation.setId(id));
    }

    @Override
    public boolean updateStatus(@NonNull Integer processId,
                                @NonNull InviteStatus status,
                                @NonNull InviteStatus newStatus) {
        Invitation invitation = new Invitation();
        invitation.setStatus(newStatus);
        invitation.setProcessId(processId);
        return update(
                invitation,
                Wrappers.<Invitation>lambdaUpdate()
                        .eq(Invitation::getStatus, status)
                        .eq(Invitation::getProcessId, processId)
        );
    }

    @Override
    public boolean hasUnsettled(Long senderId, @NonNull Integer processId) {
        return CollectionUtils.isNotEmpty(
                list(
                        Wrappers.<Invitation>lambdaQuery()
                                .eq(Invitation::getSenderId, senderId)
                                .eq(Invitation::getProcessId, processId)
                                .notIn(Invitation::getStatus, InviteStatus.unDealStatus())
                                // 只是判断有没有已经在处理的邀请，获取一个就可以了
                                .last(" limit 1 ")
                )
        );
    }

    @Override
    public int countByStatus(@NonNull Integer processId, @NonNull InviteStatus status) {
        return count(
                Wrappers.<Invitation>lambdaQuery()
                        .eq(Invitation::getStatus, status)
                        .eq(Invitation::getProcessId, processId)
        );
    }

    @Override
    public List<String> groupByTime(@NonNull Integer processId) {
        return list(
                Wrappers.<Invitation>lambdaQuery()
                        .select(Invitation::getInterviewTime)
                        .eq(Invitation::getProcessId, processId)
                        .groupBy(Invitation::getInterviewTime)
        ).stream().map(Invitation::getInterviewTime).collect(Collectors.toList());
    }

    @Override
    public boolean clean(@NonNull Invitation condition) {
        return remove(Wrappers.lambdaQuery(condition));
    }

    private void validPageParam(int pageNum, int pageSize) {
        ToolUtils.argAssert(pageNum, e -> e > 0, "页数非法");
        ToolUtils.argAssert(pageSize, e -> e > 0, "分页大小非法");
    }

}
