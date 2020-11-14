package org.xiyoulinux.join.notify.manager.strategy;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.xiyoulinux.join.notify.model.InviteStatus;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Join;
import org.xiyoulinux.join.notify.model.dao.Sender;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 排序模板策略
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:59
 */
@Log4j2
public abstract class BaseOrderDispatchStrategy implements DispatchStrategy {

    /**
     * 对报名数据进行排序
     *
     * @param joinList       报名数据
     * @param strategyConfig 策略配置
     * @return 排序后的报名数据，不能为空
     */
    protected abstract List<Join> order(List<Join> joinList, StrategyConfig strategyConfig);

    @Override
    public List<Invitation> dispatch(List<Sender> senderList,
                                     List<Join> joinList,
                                     @NonNull StrategyConfig strategyConfig) {
        if (strategyConfig == null ||
                ToolUtils.isEmptyCollection(senderList, joinList, strategyConfig.getTimeSegments())) {
            return Collections.emptyList();
        }
        List<Join> orderedList = order(joinList, strategyConfig);
        if (ToolUtils.isEmptyCollection(orderedList)) {
            throw new RuntimeException("Order strategy [" + this.getClass().getSimpleName() + "] error, order() should not be return empty");
        }

        final List<String> timeSegments = strategyConfig.getTimeSegments();
        // 这里需要是有序的
        final Map<Integer, Invitation> join2Invitation = new LinkedHashMap<>(joinList.size());
        dispatcher(timeSegments, orderedList, (time, join) ->
                join2Invitation.put(join.getId(), new Invitation()
                        .setJoinId(join.getId())
                        .setInterviewTime(time)
                        .setStatus(InviteStatus.INIT)
                        .setProcessId(strategyConfig.getProcessId())
                )
        );
        dispatcher(senderList, orderedList, (sender, join) ->
                Optional.ofNullable(join2Invitation.get(join.getId())).ifPresent(invitation -> invitation.setSenderId(sender.getId()))
        );

        return new ArrayList<>(join2Invitation.values());
    }

    /**
     * dispatchData 中的一个元素对应多个 sourceData 的元素
     */
    private <L, R> void dispatcher(List<L> dispatchData, List<R> sourceData, BiConsumer<L, R> dispatchAction) {
        int dispatchAvgSize = sourceData.size() / dispatchData.size();
        int remain = sourceData.size() % dispatchData.size();

        for (int i = 0, j = 0; i < dispatchData.size(); i++) {
            int dispatchNum = dispatchAvgSize + remain / (i + 1);
            for (int k = 0; k < dispatchNum && j < sourceData.size(); k++, j++) {
                dispatchAction.accept(dispatchData.get(i), sourceData.get(j));
            }
        }
    }

}
