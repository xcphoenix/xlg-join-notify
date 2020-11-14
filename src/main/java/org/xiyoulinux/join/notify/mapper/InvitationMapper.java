package org.xiyoulinux.join.notify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Join;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午8:02
 */
public interface InvitationMapper extends BaseMapper<Invitation> {

    /**
     * 获取未处理的报名记录
     *
     * <p>
     *     需要自定义的SQL比较少，就不写在XM里面了，这里外层不走索引，但是数据量比较小，影响不大，为了走索引再套一层没什么意义
     * </p>
     */
    @Select(
            "SELECT * FROM `join` WHERE process_id = #{processId} AND NOT EXISTS( " +
                    "SELECT `join_id` FROM `invitation` WHERE process_id = #{processId}  AND `join`.`id` = `join_id`" +
            ")"
    )
    List<Join> getUnDispatchJoin(@Param("processId") Integer processId);

}
