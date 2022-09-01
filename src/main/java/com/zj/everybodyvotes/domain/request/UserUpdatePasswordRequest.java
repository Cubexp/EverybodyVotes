package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.constant.PatternConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author cuberxp
 * @date 2021/5/10 12:00 上午
 */
@Data
public class UserUpdatePasswordRequest {
    @Pattern(regexp = PatternConstant.PASSWORD_REGEXP, message = "密码格式不对哦!")
    private String newPassword;

    @Pattern(regexp = PatternConstant.PASSWORD_REGEXP, message = "密码格式不对哦!")
    private String oldPassword;

    @NotNull
    private Long userId;
}
