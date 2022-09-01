package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.constant.PatternConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注册请求类
 * @author cuberxp
 * @date 2021/5/8 3:53 下午
 */
@Data
public class RegisterRequest {
    /**
     * 手机号
     */
    @Pattern(regexp = PatternConstant.PHONE_REGEXP, message = "手机号码格式不对哦!")
    private String phone;

    /**
     * 密码
     */
    @Pattern(regexp = PatternConstant.PASSWORD_REGEXP, message = "密码格式不对哦!")
    private String password;

    /**
     * 短信验证码
     */
    @NotBlank
    @Length(min = 5, max = 5, message = "验证码长度有问题！")
    private String code;

    /**
     * 短信验证码的redisKey
     */
    @NotBlank
    @Length(min = 20, max = 100)
    private String codeKey;
}
