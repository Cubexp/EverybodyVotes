package com.zj.everybodyvotes.domain.response;

import com.zj.everybodyvotes.constant.CommonResponseEnum;
import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/7 7:47 下午
 */
@Data
public class BaseResponse {
    protected int code;

    protected String message;

    public BaseResponse(){
        this.code = CommonResponseEnum.SUCCESS.getCode();
        this.message = CommonResponseEnum.SUCCESS.getMessage();
    }

    public BaseResponse(int code, String message){
        this.code = code;
        this.message = message;
    }
}
