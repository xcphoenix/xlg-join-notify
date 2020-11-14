package org.xiyoulinux.join.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.istack.internal.NotNull;
import org.xiyoulinux.join.notify.model.dao.Sender;
import org.xiyoulinux.join.notify.model.dto.Result;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午5:55
 */
public interface SenderService extends IService<Sender> {

    /**
     * 获取所有的通知者列表
     *
     * @return 通知者列表
     */
    List<Sender> getSenderList();

    /**
     * 批量添加通知者
     *
     * @param senders 通知者数据
     * @return 操作是否成功
     */
    boolean batchInsert(List<Sender> senders);

    /**
     * 移除通知者，如在当前状态下，要删除的通知者没有要处理的邀请（不存在或未通知），可以删除
     *
     * @param id sender id
     * @return op status
     */
    Result<Boolean> removeSender(@NotNull Long id);

}
