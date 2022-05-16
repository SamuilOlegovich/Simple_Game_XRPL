package com.samuilolegovich.model.subscribers;

import com.samuilolegovich.model.sockets.enums.StreamSubscriptionEnum;
import com.samuilolegovich.model.subscribers.interfaces.StreamSubscriber;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("streamSubscriberToAccountBalanceChanges")
public class StreamSubscriberToAccountBalanceChanges implements StreamSubscriber {
    private static final Logger LOG = LoggerFactory.getLogger(StreamSubscriberToAccountBalanceChanges.class);

    @Autowired
    RabbitTemplate template;

    @Override
    public void onSubscription(StreamSubscriptionEnum subscription, JSONObject message) {
        LOG.info("subscription returned a {} message", subscription.getMessageType());
        LOG.info("subscription returned a {} message", message.toString());
        // handle transaction || ledger message
        // обработка транзакции || сообщение бухгалтерской книги
        template.convertAndSend("rebalancing", message.toString());
    }
}
