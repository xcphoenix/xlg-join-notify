package org.xiyoulinux.join.notify.controller;

import org.springframework.web.bind.annotation.*;
import org.xiyoulinux.join.notify.model.bo.InvitationDetail;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;
import org.xiyoulinux.join.notify.model.dto.result.Result;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.service.ConfigService;
import org.xiyoulinux.join.notify.service.InvitationService;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/16 上午11:50
 */
@RestController
@RequestMapping("/")
public class InvitationController {

    private final ConfigService configService;
    private final InvitationService invitationService;

    public InvitationController(ConfigService configService, InvitationService invitationService) {
        this.configService = configService;
        this.invitationService = invitationService;
    }

    /**
     * TODO 添加学号查询，返回处理人信息
     */
    @GetMapping("/invitations/now")
    public PageResult<InvitationDetail> nowProcessDetail(@RequestParam("pageNum") int pageNum,
                                                         @RequestParam("pageSize") int pageSize,
                                                         @RequestParam(value = "joinId", required = false) Integer joinId,
                                                         @RequestParam(value = "senderId", required = false) Long senderId,
                                                         @RequestParam(value = "status", required = false) InviteStatus status,
                                                         @RequestParam(value = "time", required = false) String interviewTime) {
        Invitation condition = new Invitation();
        condition.setProcessId(getProcessId());
        condition.setJoinId(joinId);
        condition.setSenderId(senderId);
        condition.setStatus(status);
        condition.setInterviewTime(interviewTime);
        return invitationService.getInvitation(condition, pageNum, pageSize);
    }

    @GetMapping("/invitations/unsettled")
    public PageResult<Join> getUnSettled(@RequestParam("pageNum") int pageNum,
                                         @RequestParam("pageSize") int pageSize) {
        int processId = getProcessId();
        return invitationService.getUnDispatchJoin(processId, pageNum, pageSize);
    }

    @PutMapping("/invitation/{id}")
    public Result<Boolean> updateInvitation(@PathVariable("id") Long id,
                                            @RequestBody Invitation newInvitation) {
        newInvitation.setId(null);
        newInvitation.setProcessId(null);
        return Result.builder(invitationService.updateById(id, newInvitation)).success();
    }


    @GetMapping("/invitation/time")
    public PageResult<String> getInterviewTimes() {
        int processId = getProcessId();
        return PageResult.pageBuilder(invitationService.groupByTime(processId)).success();
    }

    private int getProcessId() {
        return configService.getStrategyCfg(true).getProcessId();
    }

}
