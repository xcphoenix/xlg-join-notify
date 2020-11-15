package org.xiyoulinux.join.notify.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 报名记录
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午3:10
 */
@Data
@TableName("`join`")
public class Join {

    @TableId
    private Integer id;
    private String studentNo;
    private Integer processId;
    @TableField("is_vaild")
    private boolean isValid;
    private String cnName;
    private String college;
    private String dept;
    private String adminClass;
    private String mobile;

}
