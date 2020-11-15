package org.xiyoulinux.join.notify.model.dto.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 下午2:19
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageResult<T> extends Result<List<T>> {

    private Long total;
    private Integer curPage;
    private Integer allPage;
    private boolean hasNext;

    public static <E> ResultBuilder<PageResult<E>, List<E>> pageBuilder() {
        return pageBuilder(null);
    }

    public static <E> ResultBuilder<PageResult<E>, List<E>> pageBuilder(List<E> data) {
        ResultBuilder<PageResult<E>, List<E>> resultBuilder = ResultBuilder.wrap(new PageResult<>());
        resultBuilder.setData(data);
        return resultBuilder;
    }

}
