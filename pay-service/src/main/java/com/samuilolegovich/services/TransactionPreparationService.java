package com.samuilolegovich.services;

import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.services.interfaces.TransactionPreparation;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionPreparationService implements TransactionPreparation {
    @Autowired
    private TransactionExecutionService TransactionExecutionService;

    public void prepareTransaction(CommandAnswerDto commandAnswerDto) {

    }
}
