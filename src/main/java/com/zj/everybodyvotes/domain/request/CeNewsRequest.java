package com.zj.everybodyvotes.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cuberxp
 * @date 2021/5/15 7:48 下午
 */
@Data
public class CeNewsRequest {
    @NotBlank
    private String content;

    @NotBlank
    private String title;

    @NotNull
    private Long typeId;

    @NotNull
    private Integer status;
}
