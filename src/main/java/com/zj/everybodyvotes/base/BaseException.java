package com.zj.everybodyvotes.base;

import lombok.Getter;

/**
 * 所有自定义异常的基类
 * @author cuberxp
 * @date 2021/5/8 2:45 下午
 */
@Getter
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = -8260683704882590840L;

    protected int code;

    private String message;

    protected IResponseEnum iResponseEnum;

    public BaseException(IResponseEnum iResponseEnum) {
        super(iResponseEnum.getMessage());

        this.code = iResponseEnum.getCode();
        this.message = iResponseEnum.getMessage();
    }

    public BaseException(int code, String message) {
        super(message);

        this.code = code;
        this.message = message;
    }
}
