package org.xiyoulinux.join.notify.controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.bo.ProcessStatus;
import org.xiyoulinux.join.notify.model.bo.strategy.StrategyType;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/16 下午12:06
 */
@RestController
@RequestMapping("/summary")
public class SummaryController {

    @GetMapping("/interview/processes")
    public PageResult<Pair<Integer, String>> getProcesses() {
        List<Pair<Integer, String>> processPairs = Arrays.stream(ProcessStatus.values())
                .map(e -> new ImmutablePair<>(e.getStatus(), e.getDesc()))
                .collect(Collectors.toList());
        return PageResult.pageBuilder(processPairs).success();
    }

    /*
     * 后面两个都是 string,string key是枚举字面量
     */

    @GetMapping("/strategy/types")
    public PageResult<Pair<String, String>> getStrategyTypes() {
        List<Pair<String, String>> processPairs = Arrays.stream(StrategyType.values())
                .map(e -> new ImmutablePair<>(e.name(), e.getDesc()))
                .collect(Collectors.toList());
        return PageResult.pageBuilder(processPairs).success();
    }

    @GetMapping("/interview/status")
    public PageResult<Pair<String, String>> getInterviewStatus() {
        List<Pair<String, String>> processPairs = Arrays.stream(InviteStatus.values())
                .map(e -> new ImmutablePair<>(e.name(), e.getDesc()))
                .collect(Collectors.toList());
        return PageResult.pageBuilder(processPairs).success();
    }

}
