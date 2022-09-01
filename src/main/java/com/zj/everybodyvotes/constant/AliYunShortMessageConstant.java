package com.zj.everybodyvotes.constant;

/**
 * 阿里短信息配置常量
 * @author cuberxp
 * @date 2021/5/8 4:18 下午
 */
public class AliYunShortMessageConstant {
    /**
     * 阿里账户密钥id
     */
    public static final String ACCESS_KEY_ID = "LTAI4FtLsUHDFWDy2XnctbDV";

    /**
     * 阿里账户密钥
     */
    public static final String ACCESS_KEY_SECURITY = "rR98K2BHXWvHvSFc90fkLswqt93PzD";

    /**
     * 访问的域名
     */
    public static final String EN_POINT = "dysmsapi.aliyuncs.com";

    /**
     * 签名名称
     */
    public static final String SIGN_NAME = "Cuberxp";

    /**
     * 模版CODE
     */
    public static final String TEMPLATE_CODE = "SMS_174279937";

    /**
     * 验证短长度
     */
    public static final int CODE_LEN = 5;

    /**
     * redis中的短信的key值
     */
    public static final String SHORT_MESSAGE_REDIS_KEY = "short:message:";

    /**
     * redis中的短信的key值过期时间
     */
    public static final int SHORT_MESSAGE_KEY_EXPIRATION = 5;

    /**
     * 阿里短信返回码
     * 表示接口调用成功。
     */
    public static final String OK = "OK";
}
