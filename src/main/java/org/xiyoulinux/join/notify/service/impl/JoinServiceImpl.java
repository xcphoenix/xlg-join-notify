package org.xiyoulinux.join.notify.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xiyoulinux.join.notify.mapper.JoinMapper;
import org.xiyoulinux.join.notify.model.dao.Join;
import org.xiyoulinux.join.notify.service.JoinService;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午5:50
 */
@Service
public class JoinServiceImpl implements JoinService {

    private final JoinMapper joinMapper;

    public JoinServiceImpl(JoinMapper joinMapper) {
        this.joinMapper = joinMapper;
    }

    @Override
    public List<Join> getJoinByProcessId(@NonNull Integer id) {
        return ToolUtils.pageOperation(
                (cur, size) -> joinMapper.selectPage(
                        new Page<>(cur, size),
                        Wrappers.<Join>lambdaQuery().eq(Join::getProcessId, id)
                ).getRecords()
        );
    }

    @Override
    public Join getJoinById(@NonNull Integer id) {
        return joinMapper.selectById(id);
    }

    @Override
    public Join getJoinBySno(String sno) {
        if (StringUtils.isBlank(sno)) {
            return null;
        }
        return joinMapper.selectOne(Wrappers.<Join>lambdaQuery().eq(Join::getStudentNo, sno));
    }

}
