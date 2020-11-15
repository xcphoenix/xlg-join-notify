package org.xiyoulinux.join.notify.model.dto;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午4:32
 */
public class ResultBuilder {

    public static <R extends Result<?>> R fromResp(RespCode respCode) {
        return (R) fromResp(respCode, null);
    }

    public static <R extends Result<T>, T> R fromResp(RespCode respCode, T data) {
        return (R) new Result<>(respCode.getCode(), respCode.getMessage(), respCode.isSuccess(), data);
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
