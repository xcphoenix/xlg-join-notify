package org.xiyoulinux.join.notify.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.xiyoulinux.join.notify.model.RespCode;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:31
 */
@Data
@AllArgsConstructor
public class Result<T> {

    private String code;
    private String message;
    private boolean success;
    private T data;

    public static <E> Result<E> fromResp(RespCode respCode) {
        return fromResp(respCode, null);
    }

    public static <E> Result<E> fromResp(RespCode respCode, E data) {
        return new Result<>(respCode.getCode(), respCode.getMessage(), respCode.isSuccess(), data);
    }

    public static <E> Result<E> success(String code, String message, E data) {
        return new Result<>(code, message, true, data);
    }

    public static <E> Result<E> success(String message, E data) {
        return success(RespCode.SUCCESS.getCode(), message, data);
    }

    public static <E> Result<E> success(E data) {
        return success(null, data);
    }

    public static <E> Result<E> failure(String code, String message, E data) {
        return new Result<>(code, message, false, data);
    }

    public static <E> Result<E> failure(String message, E data) {
        return failure(RespCode.FAILURE.getCode(), message, data);
    }

    public static <E> Result<E> failure(E data) {
        return failure(null, data);
    }

}
