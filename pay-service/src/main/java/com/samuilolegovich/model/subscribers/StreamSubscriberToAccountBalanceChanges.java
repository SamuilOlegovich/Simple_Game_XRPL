package com.samuilolegovich.model.subscribers;

import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.model.sockets.enums.StreamSubscriptionEnum;
import com.samuilolegovich.model.subscribers.interfaces.StreamSubscriber;
import lombok.RequiredArgsConstructor;
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
    private RabbitTemplate template;



    @Override
    public void onSubscription(StreamSubscriptionEnum subscription, JSONObject message) {
        LOG.info("subscription returned a {} message", subscription.getMessageType());
        LOG.info("subscription returned a {} message", message.toString());
        // handle transaction || ledger message
        // обработка транзакции || сообщение бухгалтерской книги

        if (message.getJSONObject("transaction").has("DestinationTag")
                && message.getJSONObject("transaction").getString("Destination")
                .equalsIgnoreCase(StringEnum.ADDRESS_FOR_SUBSCRIBE_AND_MONITOR.getValue())) {
            // из этой очереди берем ставку и адрес возврата и объем ставки
            template.convertAndSend("balance", message.toString());
        }
        else  if (message.getJSONObject("transaction").getString("Destination")
                .equalsIgnoreCase(StringEnum.ADDRESS_FOR_SUBSCRIBE_AND_MONITOR.getValue())) {
            // из этой очереди можно брать реальный баланс на кошельке (остачу)
            // не совсем правильно так как могут быть нюансы - но пока сойдет и так
            template.convertAndSend("balance-not-tag", message.toString());
        }
        else {
            template.convertAndSend("other-changes", message.toString());
        }
    }
}
