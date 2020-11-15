package org.xiyoulinux.join.notify.model.dto.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

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
    protected Long timestamp = new Date().getTime();

    /**
     * <p>需要手动填写泛型</p>
     */
    public static <E> ResultBuilder<Result<E>, E> builder() {
        return builder(null);
    }

    public static <E> ResultBuilder<Result<E>, E> builder(E data) {
        ResultBuilder<Result<E>, E> resultBuilder = ResultBuilder.wrap(new Result<>());
        resultBuilder.setData(data);
        return resultBuilder;
    }

}
