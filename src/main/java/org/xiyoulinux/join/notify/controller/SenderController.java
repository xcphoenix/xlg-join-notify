package org.xiyoulinux.join.notify.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;
import org.xiyoulinux.join.notify.model.dto.result.RespCode;
import org.xiyoulinux.join.notify.model.dto.result.Result;
import org.xiyoulinux.join.notify.model.po.Sender;
import org.xiyoulinux.join.notify.model.vo.JoinVO;
import org.xiyoulinux.join.notify.service.SenderService;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午3:24
 */
@RestController
public class SenderController {

    private final SenderService senderService;

    public SenderController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/senders")
    public Result<List<Sender>> getSenders() {
        return PageResult.pageBuilder(senderService.getSenderList()).success();
    }

    @PostMapping("/senders")
    public Result<Boolean> addSenders(@RequestBody List<Sender> senderList) {
        if (CollectionUtils.isEmpty(senderList)) {
            return Result.builder(false).success();
        }
        List<Sender> existSenders = senderService.getSenderList();
        for (Sender sender : senderList) {
            if (existSenders.contains(sender)) {
                return Result.<Boolean>builder().fromResp(RespCode.SENDER_EXISTED);
            }
        }
        senderService.batchInsert(senderList);
        return Result.builder(true).success();
    }

    @DeleteMapping("/sender/{id}")
    public Result<Boolean> delSender(@PathVariable("id") Long id) {
        return senderService.removeSender(id);
    }

    @GetMapping("/sender/joins")
    public List<JoinVO> getSenderJoins(@RequestParam("name") String username) {
        return senderService.getJoinListBySenderName(username);
    }

}
