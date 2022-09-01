package com.zj.everybodyvotes.domain.response;

import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/13 7:33 下午
 */
@Data
public class UserInfoResponse {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private Integer gender;

    private String avatar;

    private String source;

    private String uuid;

    private Long registerTime;
}
