package com.samuilolegovich.listeners;

import com.samuilolegovich.dto.BetDto;
import com.samuilolegovich.enums.ConstantsEnum;
import com.samuilolegovich.enums.Enums;
import com.samuilolegovich.enums.RedBlack;
import com.samuilolegovich.service.interfaces.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@EnableRabbit // нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListener {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private BetService betService;



    // из этой очереди берем ставку и адрес возврата и объем ставки
    @RabbitListener(queues = "queue-account-balance-changes")
    public void workerQueueAccountBalanceChanges(String message) {
        LOG.info("Accepted on workerQueueAccountBalanceChanges : " + message);
        template.convertAndSend("make-payment", betService.placeBet(getBetDto(message)));
    }

    // из этой очереди можно брать реальный баланс на кошельке (остачу)
    // не совсем правильно так как могут быть нюансы - но пока сойдет и так
    @RabbitListener(queues = "queue-account-balance-change-not-tag")
    public void workerQueueAccountBalanceChangeNotTag(String message) {
        LOG.info("Accepted on workerQueueAccountBalanceChangeNotTag : " + message);
    }

    @RabbitListener(queues = "queue-account-other-changes")
    public void workerQueueAccountOtherChanges(String message) {
        LOG.info("Accepted on workerQueueAccountOtherChanges: " + message);
    }


    private BetDto getBetDto(String message) {
        JSONObject json = new JSONObject(message);

        BigDecimal receivedFunds = new BigDecimal(json.getJSONObject("meta").getString("delivered_amount"));
        BigDecimal fundsOnTheBalanceSheet = new BigDecimal(json.getJSONObject("meta")
                .getJSONArray("AffectedNodes")
                .getJSONObject(1)
                .getJSONObject("ModifiedNode")
                .getJSONObject("FinalFields")
                .getString("Balance"));
        BigDecimal availableFunds = fundsOnTheBalanceSheet.subtract(fundsOnTheBalanceSheet);

        return BetDto.builder()
                .colorBet(getColor(json.getJSONObject("transaction").getInt("DestinationTag")))
                .userAddress(json.getJSONObject("transaction").getString("Account"))
                .data(json.getJSONObject("transaction").getLong("date"))
                .userId(UUID.randomUUID().toString())
                .availableFunds(availableFunds)
                .bet(receivedFunds)
                .build();
    }

    private Enums getColor(int i) {
        if (ConstantsEnum.ZERO.getValue() == i) {
            return RedBlack.ZERO;
        }
        if (ConstantsEnum.RED.getValue() == i) {
            return RedBlack.RED;
        }
        if (ConstantsEnum.BLACK.getValue() == i) {
            return RedBlack.BLACK;
        }
        if (ConstantsEnum.GET_LOTTO_VOLUME.getValue() == i) {
            return RedBlack.GET_LOTTO_VOLUME;
        }
        return RedBlack.OTHER;
    }
}
