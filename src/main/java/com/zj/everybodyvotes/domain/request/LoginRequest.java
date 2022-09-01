package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.constant.PatternConstant;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 登陆请求类
 * @author cuberxp
 * @date 2021/5/8 1:48 下午
 */
@Data
public class LoginRequest {
    @Pattern(regexp = PatternConstant.PHONE_REGEXP, message = "手机号码格式不对哦!")
    private String phone;

    @Pattern(regexp = PatternConstant.PASSWORD_REGEXP, message = "密码格式不对哦!")
    private String password;
}
