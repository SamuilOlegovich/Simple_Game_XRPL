package com.samuilolegovich.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControllerRabbitForTest {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerRabbitForTest.class);

    @Autowired
    AmqpTemplate template;

    // http://localhost:8080/send-a-test-message-to-the-queue
    @RequestMapping("/send-a-test-message-to-the-queue")
    @ResponseBody
    public String sendMessageToQueue() {
        LOG.info("Send to Queue Account Balance Changes");
        template.convertAndSend("queueAccountBalanceChanges","The balance has changed.");
        return "The message is sent in turn.";
    }
}
