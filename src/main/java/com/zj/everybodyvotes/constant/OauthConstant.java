package com.zj.everybodyvotes.constant;

/**
 * @author cuberxp
 * @date 2021/5/8 3:05 下午
 */
public class OauthConstant {

    /**
     * 设置记住我时token 过期时间
     * 单位毫秒
     * 十五天
     */
    public static final long EXPIRATION_TIME = 129_6000_000;

    public static final String CLAIM_TOKEN = "key";

    /**
     * token解密的key
     */
    public static final String SECURITY_KEY = "edd0230bc7a44323aae98addec27c3a5";

    /**
     * 前端放在header中的token
     */
    public static final String FRONT_POINT_HEADER_TOKEN = "t";

    /**
     * 码云平台
     */
    public static final String SOURCE_GITEE = "gitee";

    /**
     * redid oauth state pre
     */
    public static final String OAUTH_STATE_KEY = "oauth:state";

    /**
     * 己登陆用户的redis key
     */
    public static final String LOGINED_USER_REDIS_KEY = "logined:user:";

    /**
     * 登到的第三方平台数据，
     */
    public static final String OAUTH_DATA_REDIS_KEY = "oauth:data:";

    /**
     * 默认的用户头像
     */
    public static final String DEFAULT_AVATAR = "https://images.unsplash.com/photo-1620503374956-c942862f0372?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80";
}
