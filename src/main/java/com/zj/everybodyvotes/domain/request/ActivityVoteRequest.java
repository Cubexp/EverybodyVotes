package com.zj.everybodyvotes.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 活动投票
 * @author cuberxp
 * @date 2021/5/16 10:18 上午
 */
@Data
public class ActivityVoteRequest {
    /**
     * 活动id
     */
    @NotNull
    private Long activityId;

    /**
     * 投给的选手id
     */
    @NotNull
    private Long playerId;

    /**
     * 值
     */
    private Double value;

    /**
     * 投票者id
     */
    private Long votePersonId;
}
