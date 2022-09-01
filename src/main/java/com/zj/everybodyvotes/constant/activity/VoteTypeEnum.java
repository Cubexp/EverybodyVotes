package com.zj.everybodyvotes.constant.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cuberxp
 * @date 2021/5/14 8:07 下午
 */
@Getter
@AllArgsConstructor
public enum VoteTypeEnum {
    /**
     * 投票类型
     * 这里的n由playerVoteCount字段完成
     */
    TYPE_ONE(1, "一个号每天能投n票，可将全部票投给同一个选手"),
    TYPE_THREE(2, "活动时间内只能投一票")
    ;

    private final int value;

    private final String message;
}
