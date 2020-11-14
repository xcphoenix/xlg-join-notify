package org.xiyoulinux.join.notify.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyoulinux.join.notify.mapper.SenderMapper;
import org.xiyoulinux.join.notify.model.RespCode;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Sender;
import org.xiyoulinux.join.notify.model.dto.Result;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.service.ConfigService;
import org.xiyoulinux.join.notify.service.InvitationService;
import org.xiyoulinux.join.notify.service.SenderService;

import java.util.List;

/**
 * NOTE: 用户量很少，不考虑分页
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午5:55
 */
@Service
public class SenderServiceImpl extends ServiceImpl<SenderMapper, Sender> implements SenderService {

    private final InvitationService invitationService;

    private final ConfigService configService;

    public SenderServiceImpl(InvitationService invitationService, ConfigService configService) {
        this.invitationService = invitationService;
        this.configService = configService;
    }

    @Override
    public List<Sender> getSenderList() {
        return list();
    }

    @Override
    public boolean batchInsert(List<Sender> senders) {
        if (CollectionUtils.isEmpty(senders)) {
            return false;
        }
        return saveBatch(senders);
    }

    @Override
    @Transactional
    public Result<Boolean> removeSender(@NonNull Long id) {
        StrategyConfig config = configService.getStrategyCfg(true);
        if (invitationService.hasUnsettled(id, config.getProcessId())) {
            return Result.fromResp(RespCode.SENDER_UNSETTLED);
        }
        Invitation rmCondition = new Invitation().setId(id).setProcessId(config.getProcessId());
        invitationService.clean(rmCondition);
        this.removeById(id);
        return Result.success(true);
    }

}
