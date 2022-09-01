package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.base.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cuberxp
 * @date 2021/5/15 8:30 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsPageRequest extends BasePageRequest {
    private Long typeId;

    private Integer status;

    private String title;
}
