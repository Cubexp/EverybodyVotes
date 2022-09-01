package com.zj.everybodyvotes.domain.response;

import com.zj.everybodyvotes.domain.VotesActivityGrouping;
import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/15 10:10 下午
 */
@Data
public class ActivityPlayerResponse {
    private Long id;

    private Long activityId;

    private String name;

    private Long userId;

    private Long number;

    private Long views;

    /**
     * 审核是否通过
     */
    private Boolean review;

    /**
     * 描述
     */
    private String remark;

    /**
     * 分组id
     */
    private VotesActivityGrouping group;

    /**
     * 参加时间
     */
    private Long createTime;

    /**
     * 在活动中的封面图片
     */
    private String imgUrl;

    /**
     * 获取选手投票数
     */
    private Long votesCount;
}
