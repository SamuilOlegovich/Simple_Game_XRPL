package com.samuilolegovich.listeners;

import com.samuilolegovich.dto.CommandDto;
import com.samuilolegovich.service.interfaces.Bet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

@Component
@EnableRabbit // нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListener {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    private RabbitTemplate template;
    @Autowired
    @Qualifier("bet-service")
    private Bet bet;



    // передаем айдишники для ставки
    @RabbitListener(queues = "queue-account-balance-changes")
    public void workerQueueAccountBalanceChanges(String message) {
        LOG.info("Accepted on workerQueueAccountBalanceChanges : " + message);
        JSONObject json = new JSONObject(message);

        template.convertAndSend("make-payment",
                bet.placeBet(CommandDto.builder()
                        .id(json.getLong("id"))
                        .uuid(json.getString("uuid"))
                        .build()
                ));
    }

    @RabbitListener(queues = "queue-account-balance-change-not-tag")
    public void workerQueueAccountBalanceChangeNotTag(String message) {
        LOG.info("Accepted on workerQueueAccountBalanceChangeNotTag : " + message);
    }

    @RabbitListener(queues = "queue-account-other-changes")
    public void workerQueueAccountOtherChanges(String message) {
        LOG.info("Accepted on workerQueueAccountOtherChanges: " + message);
    }
}
