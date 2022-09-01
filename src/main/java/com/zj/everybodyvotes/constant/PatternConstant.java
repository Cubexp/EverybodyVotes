package com.zj.everybodyvotes.constant;

/**
 * @author cuberxp
 * @date 2021/5/8 1:51 下午
 */
public class PatternConstant {
    /**
     * 移动电话正则
     */
    public static final String PHONE_REGEXP = "(?:0|86|\\+86)?1[3-9]\\d{9}";

    /**
     * 任意数字、大小字母、特殊字符-.、6到24位之间
     */
    public static final String PASSWORD_REGEXP = "^[a-zA-Z0-9_.]{6,24}$";

    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEXP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
}
