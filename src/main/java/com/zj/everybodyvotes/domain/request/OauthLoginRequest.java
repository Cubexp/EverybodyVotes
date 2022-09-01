package com.zj.everybodyvotes.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 授权token登陆请求类
 * @author cuberxp
 * @date 2021/5/9 2:11 下午
 */
@Data
@Accessors(chain = true)
public class OauthLoginRequest {
    private String oauthToken;

    private String source;
}
