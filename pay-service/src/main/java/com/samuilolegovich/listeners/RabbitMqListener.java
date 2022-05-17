package com.samuilolegovich.listeners;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.model.paymentManager.PaymentAndSocketManagerXRPL;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@EnableRabbit // нужно для активации обработки аннотаций @RabbitListener
public class RabbitMqListener {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqListenerTest.class);

    @Autowired
    private PaymentAndSocketManagerXRPL paymentAndSocketManagerXRPL;

    @RabbitListener(queues = "to-make-a-payment")
    public void workerToMakePayment(String message) {
        LOG.info("Accepted on ToMakePayment : " + message);
        JSONObject json = new JSONObject(message);

        paymentAndSocketManagerXRPL.sendPayment(
                json.getString("winnerAddress"),
                json.getInt("lottoNow"),
                json.getBigDecimal("win"),
                BooleanEnum.IS_REAL.isB()
        );
    }

}
