package com.zj.everybodyvotes.domain.response;

import com.zj.everybodyvotes.base.IResponseEnum;

/**
 * 返回数据项的返回类
 * @author cuberxp
 * @date 2021/5/8 1:44 下午
 */
public class CommonsResponse<T> extends BaseResponse{
    private final T data;

    public CommonsResponse(T data) {
        super();
        this.data = data;
    }

    public CommonsResponse(IResponseEnum iResponseEnum, T data) {
        super(iResponseEnum.getCode(), iResponseEnum.getMessage());

        this.data = data;
    }

    public T getData() {
        return data;
    }
}
