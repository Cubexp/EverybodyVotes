package com.zj.everybodyvotes.constant.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 选手排序方式
 * @author cuberxp
 * @date 2021/5/14 9:59 下午
 */
@Getter
@AllArgsConstructor
public enum PlayerSortMethodEnum {
    /**
     * 选手排序方式
     */
    SORT_ID_HEIGHT_TO_LOW(1, "编号从低到高"),
    SORT_VOTES_HEIGHT_TO_LOW(2, "票数从高到低"),
    SORT_SING_UP_DATETIME(3, "报名时间排序")
    ;
    private final int value;

    private final String message;
}
