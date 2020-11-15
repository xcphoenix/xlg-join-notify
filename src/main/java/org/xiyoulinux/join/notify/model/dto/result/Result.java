package org.xiyoulinux.join.notify.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:31
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> {

    protected String code;
    protected String message;
    protected boolean success;
    protected T data;
    protected Long timestamp;

}
