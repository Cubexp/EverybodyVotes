package com.zj.everybodyvotes.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 主办方把选手添加到活动中
 * @author cuberxp
 * @date 2021/5/16 2:58 下午
 */
@Data
public class AddPlayerActivityRequest {
    /**
     * 参加的活动id
     */
    @NotNull
    private Long activityId;

    /**
     * 选手电话号码
     */
    @NotNull
    private String phone;

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
