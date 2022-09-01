package com.zj.everybodyvotes.constant.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 封面类型
 * @author cuberxp
 * @date 2021/5/14 7:37 下午
 */
@Getter
@AllArgsConstructor
public enum CoverTypeEnum {
    /**
     * 封面类型
     */
    IMAGE_TYPE(0, "图片类型"),
    VIDEO_TYPE(1, "视频类型")
    ;

    private final int value;

    private final String message;
}
