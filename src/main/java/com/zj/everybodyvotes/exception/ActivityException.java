package com.zj.everybodyvotes.exception;

import com.zj.everybodyvotes.base.BaseException;
import com.zj.everybodyvotes.base.IResponseEnum;
import lombok.Getter;

/**
 * @author cuberxp
 * @date 2021/5/14 9:12 下午
 */
@Getter
public class ActivityException extends BaseException {
    private static final long serialVersionUID = -6017657750975249790L;

    public ActivityException(IResponseEnum iResponseEnum) {
        super(iResponseEnum.getCode(), iResponseEnum.getMessage());
    }

    public ActivityException(int code, String message) {
        super(code, message);
    }
}
