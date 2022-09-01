package com.zj.everybodyvotes.configs;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cuberxp
 * @date 2021/5/8 2:04 下午
 */
@Configuration
public class AesConfig {

    private final static byte[] KEY = {113, 58, 75, 91, 65, 56, -69, 122, -68, -36, -90, 72, 29, -56, -6, -27};

    @Bean
    public SymmetricCrypto createSymmetricCrypto() {
        return new SymmetricCrypto(SymmetricAlgorithm.AES, KEY);
    }
}
