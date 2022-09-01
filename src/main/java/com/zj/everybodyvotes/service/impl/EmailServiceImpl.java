package com.zj.everybodyvotes.service.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.zj.everybodyvotes.service.IEmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author cuberxp
 * @date 2021/5/10 1:10 下午
 */
@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final SingleSendMailRequest singleSendMailRequest;

    private final IAcsClient client;

    @Override
    public void sendEmail(String to, String subject, String content) throws ClientException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>人人投票邮件</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        \n" +
                "        .email {\n" +
                "            position: relative;\n" +
                "            width: 100%;\n" +
                "            /* background-color: rgba(0, 0, 0, 1); */\n" +
                "        }\n" +
                "        \n" +
                "        .main {\n" +
                "            left: 0;\n" +
                "            right: 0;\n" +
                "            margin: auto;\n" +
                "            width: 80%;\n" +
                "            max-width: 800px;\n" +
                "            box-sizing: content-box;\n" +
                "        }\n" +
                "        \n" +
                "        .main .title {\n" +
                "            /* color: white; */\n" +
                "            display: inline-flex;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        \n" +
                "        .main .title span {\n" +
                "            margin: 0 10px;\n" +
                "        }\n" +
                "        \n" +
                "        .main table {\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        \n" +
                "        .main table tbody td {\n" +
                "            /* background-color: white; */\n" +
                "            padding: 20px;\n" +
                "            text-align: left;\n" +
                "            border-bottom: 1px solid rgb(161, 161, 161);\n" +
                "        }\n" +
                "        \n" +
                "        .main table td {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        \n" +
                "        tfoot td p {\n" +
                "            color: rgb(161, 161, 161);\n" +
                "            font-size: 13px;\n" +
                "        }\n" +
                "        \n" +
                "        a {\n" +
                "            color: rgb(161, 161, 161);\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        \n" +
                "        a:hover {\n" +
                "            border-bottom: 1px solid rgb(161, 161, 161);\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div class=\"email\">\n" +
                "        <div class=\"main\">\n" +
                "            <table>\n" +
                "                <thead>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <h1 class=\"title\">\n" +
                "                                <span>" + subject + "</span>\n" +
                "                            </h1>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            " + content + "\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "                <tfoot>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <p>邮件由系统自动发送，请勿直接回复。</p>\n" +
                "                            <p>官方网站：\n" +
                "                                <a href=\"http://localhost:8080\">人人投票官网</a>\n" +
                "                            </p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tfoot>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        singleSendMailRequest.setToAddress(to);
        singleSendMailRequest.setSubject("【人人投票】");
        singleSendMailRequest.setHtmlBody(html);
        client.getAcsResponse(singleSendMailRequest);

    }
}
