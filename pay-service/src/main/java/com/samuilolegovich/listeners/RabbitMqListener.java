package com.samuilolegovich.listeners;

import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.services.interfaces.TransactionPreparation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@EnableRabbit // нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListener {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListenerTest.class);

    @Autowired
    @Qualifier("transaction-preparation-service")
    private TransactionPreparation transactionPreparation;

    @RabbitListener(queues = "to-make-a-payment")
    public void workerToMakePayment(String message) {
        LOG.info("Accepted on ToMakePayment : " + message);
        JSONObject json = new JSONObject(message);

        transactionPreparation.prepareTransaction(CommandAnswerDto.builder()
                .baseUserUuid(json.getString("baseUserUuid"))
                .baseUserId(json.getLong("baseUserId"))
                .uuid(json.getString("uuid"))
                .id(json.getLong("id"))
                .build());
    }
}
