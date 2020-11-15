package org.xiyoulinux.join.notify.model.dto.result;

/**
 * 复用 PageResult、Result 功能
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午4:32
 */
public class ResultBuilder<T extends Result<E>, E> {

    private final T instance;

    private ResultBuilder(T instance) {
        this.instance = instance;
    }

    private T getInstance() {
        return instance;
    }

    /**
     * <p>
     *    wrap Result
     * </p>
     */
    public static <R extends Result<F>, F> ResultBuilder<R, F> wrap(R result) {
        return new ResultBuilder<>(result);
    }

    public T setData(E data) {
        instance.setData(data);
        return instance;
    }

    public T fromResp(RespCode respCode) {
        return fromResp(respCode, null);
    }

    public T fromResp(RespCode respCode, E data) {
        T instance = getInstance();
        instance.setCode(respCode.getCode())
                .setMessage(respCode.getMessage())
                .setSuccess(respCode.isSuccess())
                .setData(data);
        return instance;
    }

    public T success(String code, String message) {
        T instance = getInstance();
        instance.setCode(code).setMessage(message).setSuccess(true);
        return instance;
    }

    public T success(String message) {
        return success(RespCode.SUCCESS.getCode(), message);
    }

    public T success() {
        return success(null);
    }

    public T failure(String code, String message) {
        T instance = getInstance();
        instance.setCode(code).setMessage(message).setSuccess(false);
        return instance;
    }

    public T failure(String message) {
        return failure(RespCode.FAILURE.getCode(), message);
    }

    public T failure() {
        return failure(null);
    }

}
