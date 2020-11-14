package org.xiyoulinux.join.notify;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xiyoulinux.join.notify.manager.strategy.StrategyManager;
import org.xiyoulinux.join.notify.mapper.JoinMapper;
import org.xiyoulinux.join.notify.mapper.SenderMapper;
import org.xiyoulinux.join.notify.model.ProcessStatus;
import org.xiyoulinux.join.notify.model.constant.ConfigKeyConst;
import org.xiyoulinux.join.notify.model.dao.Sender;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.strategy.StrategyType;
import org.xiyoulinux.join.notify.service.ConfigService;
import org.xiyoulinux.join.notify.service.DispatchService;
import org.xiyoulinux.join.notify.service.InvitationService;
import org.xiyoulinux.join.notify.service.SenderService;
import org.xiyoulinux.join.notify.utils.SnoUtils;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Log4j2
class NotifyApplicationTests {

    @Autowired
    private JoinMapper joinMapper;

    @Autowired
    private StrategyManager strategyManager;

    @Autowired
    private SenderMapper senderMapper;

    @Autowired
    private InvitationService invitationService;

    @Test
    void testSnoUtil() {
        log.info(SnoUtils.getGrade("04172126"));
    }

    @Test
    void testToolParseMap() throws JsonProcessingException {
        String data = "G:A,B,C,D|1";
        Map<String, String> map = ToolUtils.parseMap(data);
        log.info(new ObjectMapper().writeValueAsString(map));
    }

    @Autowired
    private DispatchService dispatchService;

    @Test
    void testDispatch() {
        dispatchService.dispatch();

        // StrategyConfig strategyConfig = new StrategyConfig();
        // strategyConfig.setStrategyType(StrategyType.MATCH);
        // strategyConfig.setStrategyData("G:19,18,17|0;M:网络,计科,软件");
        // strategyConfig.setTimeSegments(Arrays.asList(
        //         "2020.11.20 - 09:00",
        //         "2020.11.20 - 09:30",
        //         "2020.11.20 - 10:00",
        //         "2020.11.20 - 10:30"
        // ));
        // DispatchStrategy dispatchStrategy = strategyManager.getStrategy(strategyConfig.getStrategyType());
        //
        // List<Sender> senderList = new ArrayList<>();
        // senderList.add(new Sender(1L, "xuanc"));
        // senderList.add(new Sender(2L, "bming"));
        // senderList.add(new Sender(3L, "cming"));
        //
        // log.info("start get list....");
        // List<Invitation> invitationList = dispatchStrategy.dispatch(
        //         senderList,
        //         ToolUtils.pageOperation(
        //                 (cur, size) -> joinMapper.selectPage(new Page<>(cur, size), null).getRecords()
        //         ),
        //         strategyConfig
        // );
        // log.info("end get list");
        // invitationList.forEach(invitation -> {
        //     log.info("{}\t{}\t{}\t{}\t{}\t{}", invitation.getJoinId(),
        //             joinMapper.selectById(invitation.getJoinId()).getStudentNo(),
        //             joinMapper.selectById(invitation.getJoinId()).getCnName(),
        //             joinMapper.selectById(invitation.getJoinId()).getAdminClass(),
        //             invitation.getInterviewTime(),
        //             invitation.getSenderId()
        //     );
        // });
    }

    @Test
    void testPingYin() throws Exception {
        HanyuPinyinOutputFormat outputF = new HanyuPinyinOutputFormat();
        outputF.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputF.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        log.info(PinyinHelper.toHanYuPinyinString("重启重庆", outputF));
    }

    @Autowired
    private ConfigService configService;

    @Test
    @Transactional
    @Rollback(value = false)
    void testConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.applyBlackRegexes("04172126", "04191148");
        strategyConfig.setProcessId(ProcessStatus.ROUND_A.getStatus());
        strategyConfig.setStrategyType(StrategyType.MATCH);
        strategyConfig.setStrategyData("G:19,18,17|0;M:网络,计科,软件");
        strategyConfig.setTimeSegments(Arrays.asList(
                "2020.11.20 - 09:00",
                "2020.11.20 - 09:30",
                "2020.11.20 - 10:00",
                "2020.11.20 - 10:30"
        ));
        configService.updateConfig(ConfigKeyConst.STRATEGY_KEY, strategyConfig);
        StrategyConfig strategyConfig2 = configService.getJsonSerialConfig(ConfigKeyConst.STRATEGY_KEY, StrategyConfig.class);
        Assertions.assertEquals(strategyConfig2, strategyConfig);
    }

    @Autowired
    private SenderService senderService;

    @Test
    @Transactional
    @Rollback(value = false)
    void testSender() {
        List<Sender> senders = Arrays.asList(
                new Sender("xuanc"),
                new Sender("bming"),
                new Sender("cming"),
                new Sender("xcphoenix")
        );
        senderService.batchInsert(senders);
        List<Sender> sendersOnDB = senderService.getSenderList();
        Assertions.assertEquals(sendersOnDB, senders);
        Long delId = sendersOnDB.get(2).getId();
        senderService.removeSender(delId);
        sendersOnDB = senderService.getSenderList();
        List<Sender> finalSendersOnDB = sendersOnDB;
        Assertions.assertTrue(() -> {
            for (Sender sender : finalSendersOnDB) {
                if (sender.getId().equals(delId)) {
                    return false;
                }
            }
            return true;
        });
    }

    @Test
    void testLambda() throws JsonProcessingException {
        Sender sender = new Sender();
        sender.setUsername("xuanc");
        List<Sender> senders = senderMapper.selectList(Wrappers.lambdaQuery(sender));
        log.info(new ObjectMapper().writeValueAsString(senders));
    }

    @Test
    @Disabled
    void testCustomSQL() {
        Assertions.assertTrue(CollectionUtils.isNotEmpty(invitationService.getUnDispatchJoin(1)));
    }

}
