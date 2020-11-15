package org.xiyoulinux.join.notify.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sender {

    @TableId
    private Long id;
    private String username;

    public Sender(String username) {
        this.username = username;
    }

}
