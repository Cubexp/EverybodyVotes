package com.zj.everybodyvotes.service;

import com.aliyuncs.exceptions.ClientException;

import javax.mail.MessagingException;

/**
 * @author cuberxp
 * @date 2021/5/10 1:08 下午
 */
public interface IEmailService {
    /**
     * 发送邮件
     * @param to 接受者邮箱
     * @param subject 主题
     * @param content 内容
     */
    void sendEmail(String to, String subject, String content) throws MessagingException, ClientException;
}
