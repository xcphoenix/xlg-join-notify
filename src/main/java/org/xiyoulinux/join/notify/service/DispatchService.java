package org.xiyoulinux.join.notify.service;

import org.xiyoulinux.join.notify.model.dto.result.Result;

import java.util.List;

/**
 * 面试通知分发服务
 * - 预览
 * - 执行分发
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/8 下午2:11
 */
public interface DispatchService {

    Result<Boolean> preview(List<Long> senders);

    Result<Boolean> dispatch();

    Result<Boolean> resetDispatch();

}
