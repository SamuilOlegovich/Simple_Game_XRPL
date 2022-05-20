package com.samuilolegovich.services.interfaces;

import com.samuilolegovich.dto.PayoutsDto;

public interface TransactionExecution {
    void executePayment(PayoutsDto payoutsDto);
}
