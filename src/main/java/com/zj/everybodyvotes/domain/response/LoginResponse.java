package com.zj.everybodyvotes.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @date 2021/5/9 2:13 下午
 */
@Data
@Accessors(chain = true)
public class LoginResponse {
    private long id;

    private String name;

    private String roleName;

    private String token;
}
