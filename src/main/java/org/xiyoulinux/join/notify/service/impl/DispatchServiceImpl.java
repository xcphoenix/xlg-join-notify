package org.xiyoulinux.join.notify.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyoulinux.join.notify.manager.strategy.DispatchStrategy;
import org.xiyoulinux.join.notify.manager.strategy.StrategyManager;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.dto.result.RespCode;
import org.xiyoulinux.join.notify.model.dto.result.Result;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.model.po.Sender;
import org.xiyoulinux.join.notify.service.*;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * NOTE: 核心逻辑，数据量较多，搞一下事务
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:25
 */
@Log4j2
@Service
public class DispatchServiceImpl implements DispatchService {

    private final InvitationService invitationService;

    private final SenderService senderService;

    private final StrategyManager strategyManager;

    private final ConfigService configService;

    public DispatchServiceImpl(InvitationService invitationService, SenderService senderService, StrategyManager strategyManager,
                               ConfigService configService, JoinService joinService) {
        this.invitationService = invitationService;
        this.senderService = senderService;
        this.strategyManager = strategyManager;
        this.configService = configService;
    }

    @Override
    @Transactional
    public Result<Boolean> dispatch() {
        StrategyConfig config = configService.getStrategyCfg(true);
        if (invitationService.countByStatus(config.getProcessId(), InviteStatus.INIT) <= 0) {
            return Result.<Boolean>builder(null).fromResp(RespCode.DISPATCH_NOT_FOUND);
        }
        // NOTE 理论上需要校验
        invitationService.updateStatus(config.getProcessId(), InviteStatus.INIT, InviteStatus.NOT_NOTIFY);
        return Result.builder(true).success();
    }

    @Override
    @Transactional
    public Result<Boolean> resetDispatch() {
        StrategyConfig config = configService.getStrategyCfg(true);
        Integer processId = config.getProcessId();
        if (invitationService.hasUnsettled(null, processId)) {
            return Result.<Boolean>builder(null).fromResp(RespCode.ALL_UNSETTLED);
        }
        // 删除所有的
        invitationService.clean(new Invitation().setProcessId(processId));
        return Result.builder(true).success();
    }

    @Override
    @Transactional
    public Result<Boolean> preview(List<Long> specialSenderIds) {
        StrategyConfig config = configService.getStrategyCfg(true);

        // 获取发送者
        List<Sender> senders;
        final List<Sender> existSenders = senderService.getSenderList();
        if (CollectionUtils.isEmpty(specialSenderIds)) {
            senders = existSenders;
        } else {
            // 只获取存在的发送者信息
            senders = existSenders.stream().filter(
                    sender -> specialSenderIds.contains(sender.getId())
            ).collect(Collectors.toList());
        }

        // 获取未处理的邀请
        List<Join> joins = ToolUtils.pageOperation(
                (cur, size) -> invitationService.getUnDispatchJoin(config.getProcessId(), cur, size).getData()
        );

        // filter
        final String[] blackRegexes = config.parseBlackRegexes();
        if (blackRegexes != null && blackRegexes.length > 0) {
            joins = joins.stream().filter(
                    e -> {
                        boolean blacked = true;
                        for (String regex : blackRegexes) {
                            if (Pattern.matches(regex, e.getStudentNo())) {
                                blacked = false;
                                break;
                            }
                        }
                        return blacked;
                    }
            ).collect(Collectors.toList());
        }

        if (ToolUtils.isEmptyCollection(senders, joins)) {
            return Result.<Boolean>builder().fromResp(RespCode.SENDER_OR_JOIN_NOT_FOUND);
        }

        // dispatch
        DispatchStrategy strategy = strategyManager.getStrategy(config.getStrategyType());
        ToolUtils.argAssert(strategy, Objects::nonNull, "invalid strategy type " + config.getStrategyType());
        List<Invitation> invitations = strategy.dispatch(senders, joins, config);

        invitationService.batchInsert(invitations);

        return Result.builder(true).success();
    }

}
