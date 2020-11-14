package org.xiyoulinux.join.notify.model.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.xiyoulinux.join.notify.model.InviteStatus;

/**
 * 面试邀请
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Invitation {

    @TableId
    private Long id;
    private Long senderId;
    private Integer joinId;
    /**
     * TODO 暂时用字符串表示，由前端处理格式转换
     */
    private String interviewTime;
    private InviteStatus status;
    /**
     * 标记描述，用于协商面试时间等方面
     */
    private String note;
    private Integer processId;

}
