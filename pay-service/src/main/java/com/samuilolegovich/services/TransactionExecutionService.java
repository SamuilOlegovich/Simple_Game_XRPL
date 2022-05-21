package com.samuilolegovich.services;

import com.samuilolegovich.dto.PayoutsDto;
import com.samuilolegovich.model.paymentManager.PaymentAndSocketManagerXRPL;
import com.samuilolegovich.services.interfaces.TransactionExecution;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Qualifier("transaction-execution-service")
public class TransactionExecutionService implements TransactionExecution {
    @Autowired
    private PaymentAndSocketManagerXRPL paymentAndSocketManagerXRPL;

    @Override
    public void executePayment(PayoutsDto payoutsDto) {
        paymentAndSocketManagerXRPL.sendPayment(payoutsDto.getAccount(),
                // вот тут возможна ошибка если у нас будет сильно большое лото
                Integer.parseInt(payoutsDto.getTagOut()),
                payoutsDto.getPayouts(), true);
    }
}
