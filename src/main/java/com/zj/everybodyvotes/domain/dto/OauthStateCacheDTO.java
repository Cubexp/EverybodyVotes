package com.zj.everybodyvotes.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author cuberxp
 * @date 2021/5/9 1:31 下午
 */
@Data
@Accessors(chain = true)
public class OauthStateCacheDTO implements Serializable {
    private static final long serialVersionUID = -7174135876583231285L;

    /**
     * 跳转前客户端待回调的客户端url
     */
    private String clientUrl;


}
