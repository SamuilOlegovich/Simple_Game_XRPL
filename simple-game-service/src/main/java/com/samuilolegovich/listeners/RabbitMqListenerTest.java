package com.samuilolegovich.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
//@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListenerTest.class);

//    Random random = new Random();
//
//    @RabbitListener(queues = "queue-account-balance-changes")
//    public void worker1(String message) {
//        LOG.info("Accepted on worker 1 : " + message);
//    }
//
//    @RabbitListener(queues = "other-changes")
//    public void worker2(String message) {
//        LOG.info("Accepted on worker 2 : " + message);
//    }
}
