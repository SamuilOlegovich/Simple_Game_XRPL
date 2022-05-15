package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.model.sockets.SocketXRP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class SocketXRPConfiguration {

    @Bean
    public SocketXRP getSocketXRP() {
        if (BooleanEnum.IS_REAL_AND_TEST_SOCKET.isB() || BooleanEnum.IS_REAL_OR_TEST_SOCKET.isB()) {
            return createNewSocket();
        }
        return null;
    }

    private SocketXRP createNewSocket() {
        try {
            return new SocketXRP(StringEnum.WSS_REAL.getValue());
        } catch (URISyntaxException e) { e.printStackTrace(); }
        return null;
    }
}
