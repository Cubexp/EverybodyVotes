package com.zj.everybodyvotes.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 活加选手请求参数封状类
 * @author cuberxp
 * @date 2021/5/15 3:10 下午
 */
@Data
public class CePlayerRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private String remark;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 音频url
     */
    private String audioUrl;

    /**
     * 分组id 如果分组没有开启的话，分组可以为空
     */
    private Long groupId;

    /**
     * 选手封面图
     */
    @NotBlank
    private String imgUrl;
}
