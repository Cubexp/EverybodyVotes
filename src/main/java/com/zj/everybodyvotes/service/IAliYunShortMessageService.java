package com.zj.everybodyvotes.service;

/**
 * 阿里短信息服务
 * @author cuberxp
 * @date 2021/5/8 4:15 下午
 */
public interface IAliYunShortMessageService {

    /**
     * 发送送短信
     * @param phone 手机号
     * @return 验证码的key
     * @exception Exception 注释好难写不写了。。。
     */
    String sendShortMessage(String phone) throws Exception;

    /**
     * 验证短信验证码
     * @param codeKey 注释好难写不写了。。。
     */
    void checkShortMessage(String codeKey, String code);
}
