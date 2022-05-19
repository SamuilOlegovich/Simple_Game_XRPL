package com.samuilolegovich.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControllerRabbitForTest {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerRabbitForTest.class);

    @Autowired
    RabbitTemplate template;



    // http://localhost:8080/
    @ResponseBody
    @RequestMapping("/")
    public String home() {
        return "Empty mapping";
    }

    // http://localhost:8080/send-test-message/rebalancing.other-changes/баланс_изменился_=_100XRP/
    @ResponseBody
    @RequestMapping("/send-test-message/{key}/{message}")
    public String error(@PathVariable("key") String key, @PathVariable("message") String message) {
        LOG.info(String.format("Send '%s' to '%s'", message, key));
        template.convertAndSend(key, message);
        return String.format("Send '%s' to '%s'",message,key);
    }
}
