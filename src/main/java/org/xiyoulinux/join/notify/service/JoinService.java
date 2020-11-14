package org.xiyoulinux.join.notify.service;

import com.sun.istack.internal.NotNull;
import org.xiyoulinux.join.notify.model.dao.Join;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午5:47
 */
public interface JoinService {

    List<Join> getJoinByProcessId(@NotNull Integer id);

    Join getJoinById(@NotNull Integer id);

    Join getJoinBySno(String sno);

}
