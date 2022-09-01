package com.zj.everybodyvotes.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author cuberxp
 * @date 2021/5/16 2:58 下午
 */
@Data
public class EnterActivityRequest {
    /**
     * 参加的活动id
     */
    @NotNull
    private Long activityId;

    /**
     * 报名者id
     */
    @NotNull
    private Long userId;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 描述
     */
    private String remark;

    /**
     * 在活动中的封面图片
     */
    private String imgUrl;
}
