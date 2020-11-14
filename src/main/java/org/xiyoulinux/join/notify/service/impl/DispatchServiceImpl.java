package org.xiyoulinux.join.notify.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyoulinux.join.notify.manager.strategy.DispatchStrategy;
import org.xiyoulinux.join.notify.manager.strategy.StrategyManager;
import org.xiyoulinux.join.notify.model.InviteStatus;
import org.xiyoulinux.join.notify.model.RespCode;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Join;
import org.xiyoulinux.join.notify.model.dao.Sender;
import org.xiyoulinux.join.notify.model.dto.Result;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.service.*;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.Collections;
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

    private final JoinService joinService;

    public DispatchServiceImpl(InvitationService invitationService, SenderService senderService, StrategyManager strategyManager,
                               ConfigService configService, JoinService joinService) {
        this.invitationService = invitationService;
        this.senderService = senderService;
        this.strategyManager = strategyManager;
        this.configService = configService;
        this.joinService = joinService;
    }

    @Override
    @Transactional
    public List<Invitation> previewDispatch() {
        StrategyConfig config = configService.getStrategyCfg(true);

        // get joins, sender
        final List<Sender> senders = senderService.getSenderList();
        List<Join> joins = joinService.getJoinByProcessId(config.getProcessId());
        if (ToolUtils.isEmptyCollection(senders, joins)) {
            log.warn("senders empty or joins empty");
            return Collections.emptyList();
        }

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

        // dispatch
        DispatchStrategy strategy = strategyManager.getStrategy(config.getStrategyType());
        ToolUtils.argAssert(strategy, Objects::nonNull, "invalid strategy type " + config.getStrategyType());
        List<Invitation> invitations = strategy.dispatch(senders, joins, config);

        // NOTE 理论上需要再次校验
        invitationService.batchInsert(invitations);

        return invitations;
    }

    @Override
    @Transactional
    public void dispatch() {
        StrategyConfig config = configService.getStrategyCfg(true);
        if (invitationService.countByStatus(config.getProcessId(), InviteStatus.INIT) <= 0) {
            this.previewDispatch();
        }
        // NOTE 理论上需要校验
        invitationService.updateStatus(config.getProcessId(), InviteStatus.INIT, InviteStatus.NOT_NOTIFY);
    }

    @Override
    @Transactional
    public Result<Boolean> resetDispatch() {
        StrategyConfig config = configService.getStrategyCfg(true);
        Integer processId = config.getProcessId();
        if (invitationService.hasUnsettled(null, processId)) {
            return Result.fromResp(RespCode.ALL_UNSETTLED);
        }
        // 删除所有的
        invitationService.clean(new Invitation().setProcessId(processId));
        return Result.success(true);
    }

}
