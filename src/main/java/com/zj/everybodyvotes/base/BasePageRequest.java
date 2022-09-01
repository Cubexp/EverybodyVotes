package com.zj.everybodyvotes.base;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author cuberxp
 * @date 2021/5/14 11:48 下午
 */
@Data
public class BasePageRequest {
    @NotNull
    protected Integer currentPage;

    @NotNull
    protected Integer limit;
}
