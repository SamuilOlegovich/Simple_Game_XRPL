package com.samuilolegovich.model.subscribers;

import com.samuilolegovich.domain.User;
import com.samuilolegovich.domain.UserTest;
import com.samuilolegovich.dto.CommandDto;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.model.sockets.enums.StreamSubscriptionEnum;
import com.samuilolegovich.model.subscribers.interfaces.StreamSubscriber;
import com.samuilolegovich.repository.UserRepo;
import com.samuilolegovich.repository.UserRepoTest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Qualifier("streamSubscriberToAccountBalanceChangesTest")
public class StreamSubscriberToAccountBalanceChangesTest implements StreamSubscriber {
    private static final Logger LOG = LoggerFactory.getLogger(StreamSubscriberToAccountBalanceChanges.class);

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private UserRepoTest userRepoTest;



    @Override
    public void onSubscription(StreamSubscriptionEnum subscription, JSONObject message) {
        LOG.info("subscription returned a {} message", subscription.getMessageType());
        LOG.info("subscription returned a {} message", message.toString());

        if (message.has("transaction")
                && message.getJSONObject("transaction").has("DestinationTag")
                && message.getJSONObject("transaction").getString("Destination")
                .equalsIgnoreCase(StringEnum.ADDRESS_FOR_SUBSCRIBE_AND_MONITOR_TEST.getValue())) {
            // если в сообщении есть поле тег, а так же адресат получателя мой кошелек
            BigDecimal receivedFunds = new BigDecimal(message.getJSONObject("meta").getString("delivered_amount"));
            BigDecimal fundsOnTheBalanceSheet = new BigDecimal(message.getJSONObject("meta")
                    .getJSONArray("AffectedNodes")
                    .getJSONObject(1)
                    .getJSONObject("ModifiedNode")
                    .getJSONObject("FinalFields")
                    .getString("Balance"));
            BigDecimal availableFunds = fundsOnTheBalanceSheet.subtract(fundsOnTheBalanceSheet);

            String uuid = UUID.randomUUID().toString();

            Long id = userRepoTest.save(UserTest.builder()
                    .destinationTag(message.getJSONObject("transaction").getInt("DestinationTag") + "")
                    .data(message.getJSONObject("transaction").getBigDecimal("date").toString())
                    .account(message.getJSONObject("transaction").getString("Account"))
                    .bet(roundTheBet(receivedFunds))
                    .availableFunds(availableFunds)
                    .uuid(uuid)
                    .build())
                    .getId();

            // из этой очереди получаем айди ставки - заходим в базу - находим и обрабатываем
            template.convertAndSend(StringEnum.BALANCE_ROUTING_KEY_TEST.getValue(), CommandDto.builder().id(id).uuid(uuid).build());

        } else  if (message.has("transaction")
                && message.getJSONObject("transaction").has("Destination")
                && message.getJSONObject("transaction").getString("Destination")
                .equalsIgnoreCase(StringEnum.ADDRESS_FOR_SUBSCRIBE_AND_MONITOR_TEST.getValue())) {
            // если адрес получателя мой кошелек, но тега нет
            template.convertAndSend(StringEnum.BALANCE_NOT_TAG_ROUTING_KEY_TEST.getValue(), message.toString());

        } else {
            // сообщения любого другого характера
            template.convertAndSend(StringEnum.OTHER_CHANGES_ROUTING_KEY_TEST.getValue(), message.toString());
        }
    }

    private BigDecimal roundTheBet(BigDecimal bet) {
        StringBuilder stringBuilder = new StringBuilder(bet.toString());
        if (stringBuilder.length() > 4) {
            stringBuilder.replace(stringBuilder.length() - 4, stringBuilder.length(), "0000");
        }
        return new BigDecimal(stringBuilder.toString());
    }
}
