package com.zj.everybodyvotes.configs;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里邮件发送配置
 * @author cuberxp
 * @date 2021/5/10 1:02 下午
 */
@Configuration
public class MailConfig {
    /**
     * 需要自己配置相应的阿里邮件配置
     */
   @Bean
   public IAcsClient iAcsClient() {
      IClientProfile profile = DefaultProfile.getProfile("", "", "");
      return new DefaultAcsClient(profile);
   }

   @Bean
    public SingleSendMailRequest mailAccount() {
      SingleSendMailRequest request = new SingleSendMailRequest();
      request.setAccountName("");
      request.setFromAlias("");
      request.setAddressType(1);
      request.setReplyToAddress(true);
      request.setMethod(MethodType.POST);
      return request;
   }
}
