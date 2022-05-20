package com.samuilolegovich.listeners;

import com.samuilolegovich.dto.CommandAnswerDto;
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
public class RabbitMqListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListenerTest.class);

    @RabbitListener(queues = "queue-account-balance-changes")
    public void worker1(String message) {
        LOG.info("Accepted on worker 1 : " + message);
    }

    @RabbitListener(queues = "other-changes")
    public void worker2(String message) {
        LOG.info("Accepted on worker 2 : " + message);
    }


    @Autowired
    @Qualifier("transaction-preparation-test")
    private TransactionPreparation transactionPreparationTest;

    @RabbitListener(queues = "to-make-a-payment-test")
    public void workerToMakePaymentTest(String message) {
        LOG.info("Accepted on ToMakePaymentTest : " + message);
        JSONObject json = new JSONObject(message);

        transactionPreparationTest.prepareTransaction(CommandAnswerDto.builder()
                .baseUserUuid(json.getString("baseUserUuid"))
                .baseUserId(json.getLong("baseUserId"))
                .uuid(json.getString("uuid"))
                .id(json.getLong("id"))
                .build());
    }
}
