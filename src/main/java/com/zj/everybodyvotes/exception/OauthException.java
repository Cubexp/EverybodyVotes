package com.zj.everybodyvotes.exception;

import com.zj.everybodyvotes.base.BaseException;
import com.zj.everybodyvotes.base.IResponseEnum;
import lombok.Getter;

/**
 * 登陆异常
 * @author cuberxp
 * @date 2021/5/7 9:50 下午
 */
@Getter
public class OauthException extends BaseException {
    private static final long serialVersionUID = -6844381932370452598L;

    public OauthException(IResponseEnum iResponseEnum) {
        super(iResponseEnum);
    }

    public OauthException(int code, String message) {
        super(code, message);
    }
}
