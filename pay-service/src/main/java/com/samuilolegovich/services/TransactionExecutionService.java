package com.samuilolegovich.services;

import com.samuilolegovich.model.paymentManager.PaymentAndSocketManagerXRPL;
import com.samuilolegovich.services.interfaces.TransactionExecution;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionExecutionService implements TransactionExecution {
    @Autowired
    private PaymentAndSocketManagerXRPL paymentAndSocketManagerXRPL;
}
