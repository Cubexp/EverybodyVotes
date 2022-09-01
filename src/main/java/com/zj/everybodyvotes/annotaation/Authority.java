package com.zj.everybodyvotes.annotaation;

import com.zj.everybodyvotes.constant.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * @author cuberxp
 * @date 2021/5/8 1:06 下午
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {
    boolean required() default true;
    RoleEnum[] role() default RoleEnum.USER;
}
