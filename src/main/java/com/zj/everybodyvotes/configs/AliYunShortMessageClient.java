package com.zj.everybodyvotes.configs;

import com.aliyun.teaopenapi.models.Config;
import com.zj.everybodyvotes.constant.AliYunShortMessageConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里短信息配置类
 * @author cuberxp
 * @date 2021/5/8 4:26 下午
 */
@Configuration
public class AliYunShortMessageClient {

    @Bean
    public com.aliyun.dysmsapi20170525.Client createShortMessageClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(AliYunShortMessageConstant.ACCESS_KEY_ID)
                .setAccessKeySecret(AliYunShortMessageConstant.ACCESS_KEY_SECURITY);

        config.endpoint = AliYunShortMessageConstant.EN_POINT;
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}
