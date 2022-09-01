package com.zj.everybodyvotes.base;

/**
 * @author cuberxp
 * @date 2021/5/8 2:42 下午
 */
public interface IResponseEnum {
    /**
     * 获取返回码
     * @return 返回码
     */
    int getCode();

    /**
     * 获取返回信息
     * @return 返回信息
     */
    String getMessage();
}
