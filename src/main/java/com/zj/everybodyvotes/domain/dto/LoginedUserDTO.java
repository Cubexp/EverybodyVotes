package com.zj.everybodyvotes.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 己登陆的用户信息保存到redis中，方便查看用户的权限信息
 * @author cuberxp
 * @date 2021/5/9 2:24 下午
 */
@Data
@Accessors(chain = true)
public class LoginedUserDTO implements Serializable {
    private static final long serialVersionUID = 8055339010008626834L;

    private Long userId;

    private String roleName;
}
