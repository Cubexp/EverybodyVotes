package com.zj.everybodyvotes.constant.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cuberxp
 * @date 2021/5/14 9:01 下午
 */
@Getter
@AllArgsConstructor
public enum VoteButtonTypeEnum {
    /**
     * 投票按钮类型输入分数
     */
    INPUT_TYPE(1, "点赞"),
    ONCLICK_TYPE(3, "投票"),
    THREE_TYPE(2, "打分")
    ;

    private final int value;

    private final String message;
}
