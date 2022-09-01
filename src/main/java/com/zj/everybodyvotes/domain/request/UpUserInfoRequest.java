package com.zj.everybodyvotes.domain.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * @author cuberxp
 * @date 2021/5/10 12:27 下午
 */
@Data
public class UpUserInfoRequest {
    @Length(min = 1, max = 30, message = "名称格式不对")
    private String name;

    @Range(min = 0, max = 1)
    private Integer gender;

    @Length(max = 255)
    private String avatar;
}
