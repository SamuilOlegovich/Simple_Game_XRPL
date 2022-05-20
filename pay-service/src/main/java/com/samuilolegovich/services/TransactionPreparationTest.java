package com.samuilolegovich.services;

import com.samuilolegovich.domain.PayoutsTest;
import com.samuilolegovich.domain.UserTest;
import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.PayoutsDto;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.repository.PayoutsRepoTest;
import com.samuilolegovich.repository.UserRepoTest;
import com.samuilolegovich.services.interfaces.TransactionExecution;
import com.samuilolegovich.services.interfaces.TransactionPreparation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Qualifier("transaction-preparation-test")
public class TransactionPreparationTest implements TransactionPreparation {
    @Autowired
    @Qualifier("transaction-execution-test")
    private TransactionExecution transactionExecutionTest;
    @Autowired
    private PayoutsRepoTest payoutsRepoTest;
    @Autowired
    private UserRepoTest userRepoTest;

    public void prepareTransaction(CommandAnswerDto commandAnswerDto) {
        if (!commandAnswerDto.getUuid().equalsIgnoreCase(StringEnum.PLAYER_NOT_FOUND.getValue())) {
            Optional<PayoutsTest> optionalPayoutsTest
                    = payoutsRepoTest.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());
            Optional<UserTest> optionalUserTest
                    = userRepoTest.findByIdAndUuid(commandAnswerDto.getBaseUserId(), commandAnswerDto.getBaseUserUuid());

            if (optionalPayoutsTest.isPresent() && optionalUserTest.isPresent()) {
                transactionExecutionTest.executePayment(PayoutsDto.builder()
                        .availableFunds(optionalPayoutsTest.get().getAvailableFunds())
                        .destinationTag(optionalPayoutsTest.get().getDestinationTag())
                        .account(optionalPayoutsTest.get().getAccount())
                        .payouts(optionalPayoutsTest.get().getPayouts())
                        .tagOut(optionalPayoutsTest.get().getTagOut())
                        .data(optionalPayoutsTest.get().getData())
                        .uuid(optionalPayoutsTest.get().getUuid())
                        .bet(optionalPayoutsTest.get().getBet())
                        .id(optionalPayoutsTest.get().getId())
                        .build());
                payoutsRepoTest.delete(optionalPayoutsTest.get());
                userRepoTest.delete(optionalUserTest.get());
            }

        } else if (!commandAnswerDto.getUuid().equalsIgnoreCase(StringEnum.DONATION.getValue())) {

            Optional<PayoutsTest> optionalPayoutsTest
                    = payoutsRepoTest.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());

            if (optionalPayoutsTest.isPresent()) {
                transactionExecutionTest.executePayment(PayoutsDto.builder()
                        .availableFunds(optionalPayoutsTest.get().getAvailableFunds())
                        .destinationTag(optionalPayoutsTest.get().getDestinationTag())
                        .account(optionalPayoutsTest.get().getAccount())
                        .payouts(optionalPayoutsTest.get().getPayouts())
                        .tagOut(optionalPayoutsTest.get().getTagOut())
                        .data(optionalPayoutsTest.get().getData())
                        .uuid(optionalPayoutsTest.get().getUuid())
                        .bet(optionalPayoutsTest.get().getBet())
                        .id(optionalPayoutsTest.get().getId())
                        .build());
                payoutsRepoTest.delete(optionalPayoutsTest.get());
            }
        }
    }
}
