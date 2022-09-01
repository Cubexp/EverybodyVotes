package com.zj.everybodyvotes.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 授权登陆响应类
 * @author cuberxp
 * @date 2021/5/9 1:48 下午
 */
@Data
@Accessors(chain = true)
public class OauthResponse {
    private String source;

    private String oauthToken;

    private String clientUrl;
}
