package com.zj.everybodyvotes.constant;

import com.zj.everybodyvotes.base.IResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author cuberxp
 * @date 2021/5/7 9:54 下午
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements IResponseEnum {
    /**
     * 一般请求数据成功返回
     */
    SUCCESS(200, "SUCCESS"),
    PASSWORD_ERROR(3000, "用户名或密码错误！"),
    OAUTH_ERROR(401, "无权限访问!"),
    SHORT_MESSAGE_VERIFY_CODE_EXPIRATION(3001, "验证码己过期!"),
    SHORT_MESSAGE_VERIFY_CODE_NOT_EQUAL(3002, "验证码不一至!"),
    OAUTH_NOT_SUPPORT_SOURCE(3003, "暂不支持的第三方登陆平台"),
    TOKEN_TIMEOUT(401, "token己过期"),
    OLD_NEW_PASSWORD_EQUAL(3004, "新旧密码不能相同!"),
    OLD_PASSWORD_ERROR(3005, "旧密码错误!"),
    EMAIL_ONLY_ERROR(3006, "邮箱己存在，请换一个!"),
    PHONE_ONLY_ERROR(3007, "手机号码己存在，请换一个"),
    USER_NOT_EXIST_ERROR(3008, "用户不存在!"),
    FILE_ERROR(3009, "文件格式错误！"),
    DEFAULT_WEBSITE_URL(1, "http://localhost:8080/#/activity/"),
    DEFAULT_PASSWORD(2, "123456")
    ;

    /**
     * 返回码
     */
    private final int code;

    /**
     * 返回信息
     */
    private final String message;

    /**
     * 根据message 得到状态码
     * 确认错误位置
     * @param message 错误信息
     * @return 状态码
     */
    public static int getCodeWithMessage(String message) {
        return Arrays.stream(CommonResponseEnum.values())
                .filter(commonResponseEnum -> commonResponseEnum.message.equals(message))
                .map(commonResponseEnum -> commonResponseEnum.code)
                .findFirst().orElse(500);
    }
}
