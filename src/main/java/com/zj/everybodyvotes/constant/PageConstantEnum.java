package com.zj.everybodyvotes.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认页码参数
 * @author cuberxp
 * @date 2021/5/7 9:57 下午
 */
@Getter
@AllArgsConstructor
public enum PageConstantEnum {
    /**
     * 默认的当前页
     */
    DEFAULT_PAGE_NO(1, "默认的当前页"),

    /**
     * 每页的记录数
     */
    DEFAULT_PAGE_SIZE(10, "每页的记录数");

    /**
     * 值
     */
    private final int value;

    /**
     * 字段描述
     */
    private final String message;
}
