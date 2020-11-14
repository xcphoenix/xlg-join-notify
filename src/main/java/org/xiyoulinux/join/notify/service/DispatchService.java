package org.xiyoulinux.join.notify.service;

import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dto.Result;

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

    List<Invitation> previewDispatch();

    void dispatch();

    Result<Boolean> resetDispatch();

}
