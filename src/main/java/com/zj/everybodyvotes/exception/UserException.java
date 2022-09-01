package com.zj.everybodyvotes.exception;

import com.zj.everybodyvotes.base.BaseException;
import com.zj.everybodyvotes.base.IResponseEnum;
import lombok.Getter;

/**
 * @author cuberxp
 * @date 2021/5/10 12:07 上午
 */
@Getter
public class UserException  extends BaseException {
    private static final long serialVersionUID = -7188630710316049630L;

    public UserException(IResponseEnum iResponseEnum) {
        super(iResponseEnum);
    }

    public UserException(int code, String message) {
        super(code, message);
    }
}
