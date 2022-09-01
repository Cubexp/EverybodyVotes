package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.base.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author cuberxp
 * @date 2021/5/15 10:18 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityPlayerPageRequest extends BasePageRequest {
    @NotNull
    private Integer activityId;

    /**
     * 排序方式
     * 1. 票数
     * 2. 编剧号
     */
    private Integer sortType;

    private Boolean status;

    private String playerName;

    private String phone;

    private Integer groupId;
}
