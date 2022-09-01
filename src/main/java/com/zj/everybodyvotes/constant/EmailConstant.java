package com.zj.everybodyvotes.constant;

/**
 * 邮臬常量
 * @author cuberxp
 * @date 2021/5/10 1:41 下午
 */
public class EmailConstant {
    /**
     * redis中的邮箱验证码的key值过期时间
     */
    public static final int SHORT_MESSAGE_KEY_EXPIRATION = 5;

    /**
     * redis中的短信的key值
     */
    public static final String SHORT_MESSAGE_REDIS_KEY = "email:message:";

}
