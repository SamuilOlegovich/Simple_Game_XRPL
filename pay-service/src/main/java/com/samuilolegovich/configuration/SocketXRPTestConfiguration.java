package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.model.sockets.SocketXRPTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class SocketXRPTestConfiguration {
    @Bean
    public SocketXRPTest getSocketXRPTest() {
        if (BooleanEnum.IS_REAL_AND_TEST_SOCKET.isB() || !BooleanEnum.IS_REAL_OR_TEST_SOCKET.isB()) {
            return createNewSocket();
        }
        return null;
    }

    private SocketXRPTest createNewSocket() {
        try {
            return new SocketXRPTest(StringEnum.WSS_TEST.getValue());
        } catch (URISyntaxException e) { e.printStackTrace(); }
        return null;
    }
}
