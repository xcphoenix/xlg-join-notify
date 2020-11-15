package org.xiyoulinux.join.notify.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午2:19
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageResult<T> extends Result<List<T>> {

    private Long total;
    private Integer curPage;
    private Integer allPage;
    private boolean hasNext;


}
