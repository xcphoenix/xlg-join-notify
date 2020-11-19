package org.xiyoulinux.join.notify.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 兼容sms前端
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/19 下午3:19
 */
@Data
public class JoinVO {

    @JsonProperty("admin_class")
    private String adminClass;

    @JsonProperty("cn_name")
    private String cnName;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("process_id")
    private Integer processId;

    @JsonProperty("student_no")
    private String studentNo;

    @JsonProperty("time_segment")
    private String timeSegment;

}
