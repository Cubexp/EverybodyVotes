package com.zj.everybodyvotes.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.zj.everybodyvotes.constant.AliYunShortMessageConstant;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.exception.OauthException;
import com.zj.everybodyvotes.service.IAliYunShortMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author cuberxp
 * @date 2021/5/8 4:17 下午
 */
@Service
@AllArgsConstructor
public class AliYunShortMessageImpl implements IAliYunShortMessageService {

    private final Client client;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String sendShortMessage(String phone) throws Exception {
        String redisKey = IdUtil.simpleUUID();

        redisKey = AliYunShortMessageConstant.SHORT_MESSAGE_REDIS_KEY + redisKey;

        //获取验证码
        String verifyCode = randomVerifyString();

        sendMessage(phone, verifyCode);

        // 验证码有效期5分钟
        redisTemplate.opsForValue().set(redisKey, verifyCode, AliYunShortMessageConstant.SHORT_MESSAGE_KEY_EXPIRATION, TimeUnit.MINUTES);
        return redisKey;
    }

    private void sendMessage(String phone, String verifyCode) throws Exception {
        JSONObject jsonObject = new JSONObject(1);
        jsonObject.put("code", verifyCode);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(AliYunShortMessageConstant.SIGN_NAME)
                .setTemplateCode(AliYunShortMessageConstant.TEMPLATE_CODE)
                .setTemplateParam(jsonObject.toJSONString());
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        SendSmsResponseBody body = sendSmsResponse.getBody();

        System.out.println(body.getCode() + " " + body.getMessage());
        if (!AliYunShortMessageConstant.OK.equals(body.getCode())) {
            throw new OauthException(403, body.getMessage());
        }
    }

    private String randomVerifyString() {
        StringBuilder verifyString = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < AliYunShortMessageConstant.CODE_LEN; i++) {
            verifyString.append(secureRandom.nextInt(10));
        }

        return verifyString.toString();
    }

    @Override
    public void checkShortMessage(String codeKey, String userCode) {
        String redisCode = Optional.ofNullable(redisTemplate.opsForValue().get(codeKey))
                .map(Object::toString)
                .orElseThrow(() -> new OauthException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_EXPIRATION));
        if (!userCode.equals(redisCode)) {
            throw new OauthException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_NOT_EQUAL);
        }
    }
}
