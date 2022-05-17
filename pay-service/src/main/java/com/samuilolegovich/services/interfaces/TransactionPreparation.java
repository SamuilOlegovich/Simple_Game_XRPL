package com.samuilolegovich.services.interfaces;

import com.samuilolegovich.dto.CommandAnswerDto;

public interface TransactionPreparation {
    void prepareTransaction(CommandAnswerDto commandAnswerDto);
}
