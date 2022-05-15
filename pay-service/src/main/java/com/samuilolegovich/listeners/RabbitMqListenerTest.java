package com.samuilolegovich.listeners;

import com.samuilolegovich.configuration.RabbitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitConfiguration.class);

    @RabbitListener(queues = "queueAccountBalanceChanges")
    public void processQueue(String message) {
        LOG.info("Received from queue Account Balance Changes: " + message);
    }

//    @RabbitListener(queues = "queueAccountBalanceChanges")
//    public void processQueueTwo(Message message) {
//        LOG.info("Received from queue Account Balance Changes: " + new String(message.getBody()));
//    }
}
