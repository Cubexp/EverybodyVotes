package com.zj.everybodyvotes.configs;

import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.BucketAuthorization;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileBucketLocalAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import com.zj.everybodyvotes.constant.UCloudProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cuberxp
 * @date 2021/5/10 12:15 下午
 */
@Configuration
public class UCloudConfig {

    /**
     * 本地Bucket相关API的签名器（账号在ucloud 的API 公私钥，不能使用token）
     * 如果只用到了文件操作，不需要配置下面的bucket 操作公私钥
     */
    @Bean
    public BucketAuthorization createBuckAuthorization() {
        return new UfileBucketLocalAuthorization(UCloudProperties.PUBLIC_KEY, UCloudProperties.PRIVATE_KEY);
    }

    /**
     * 本地Object相关API的签名器
     */
    @Bean
    public ObjectAuthorization createObjectAuthorization() {
        return new UfileObjectLocalAuthorization(UCloudProperties.PUBLIC_KEY, UCloudProperties.PRIVATE_KEY);
    }

    /**
     * 对象存储默认的设置
     */
    @Bean
    public ObjectConfig createObjectConfig() {
        return new ObjectConfig(UCloudProperties.OBJECT_CONFIG_REGION, UCloudProperties.OBJECT_CONFIG_PROXY_SUFFIX);
    }
}
