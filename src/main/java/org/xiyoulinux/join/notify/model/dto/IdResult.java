package org.xiyoulinux.join.notify.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author      xuanc
 * @date        2020/11/8 下午2:37
 * @version     1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class IdResult<T> extends Result<T> {

    private String resultId;

    public IdResult(String code, String message, boolean success, T data) {
        super(code, message, success, data);
    }

}
