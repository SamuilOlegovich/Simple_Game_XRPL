package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.model.wallets.WalletXRP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WalletXRPConfiguration {
    @Bean
    public WalletXRP getWalletXRPt() {
        if (BooleanEnum.IS_REAL_AND_TEST_SOCKET.isB() || BooleanEnum.IS_REAL_OR_TEST_SOCKET.isB()) {
            return new WalletXRP();
        }
        return null;
    }
}
