package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.constant.PatternConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author cuberxp
 * @date 2021/5/10 2:03 下午
 */
@Data
public class UpUserPhoneRequest {
    @Pattern(regexp = PatternConstant.PHONE_REGEXP, message = "手机号码格式不对!")
    private String newPhone;

    @NotBlank
    @Length(min = 5, max = 5, message = "验证码长度有问题！")
    private String verifyCode;

    @NotBlank
    @Length(min = 3, max = 100)
    private String verifyCodeKey;
}
