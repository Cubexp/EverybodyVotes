package com.zj.everybodyvotes.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限相关的枚举常量
 * @author cuberxp
 * @date 2021/5/8 1:03 下午
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN(0, "admin"),
    USER(1, "user"),
    TOURIST(2,"visitor");

    private final int id;

    private final String roleName;
}
