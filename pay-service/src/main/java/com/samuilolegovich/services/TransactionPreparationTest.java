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
        if (!commandAnswerDto.getUuid().equalsIgnoreCase(StringEnum.PLAYER_NOT_FOUND.getValue())
                && !commandAnswerDto.getBaseUserUuid().equalsIgnoreCase(StringEnum.DONATION.getValue())) {

            Optional<PayoutsTest> optionalPayouts
                    = payoutsRepoTest.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());
            Optional<UserTest> optionalUser
                    = userRepoTest.findByIdAndUuid(commandAnswerDto.getBaseUserId(), commandAnswerDto.getBaseUserUuid());

            if (optionalPayouts.isPresent() && optionalUser.isPresent()) {
                transactionExecutionTest.executePayment(PayoutsDto.builder()
                        .availableFunds(optionalPayouts.get().getAvailableFunds())
                        .destinationTag(optionalPayouts.get().getDestinationTag())
                        .account(optionalPayouts.get().getAccount())
                        .payouts(optionalPayouts.get().getPayouts())
                        .tagOut(optionalPayouts.get().getTagOut())
                        .data(optionalPayouts.get().getData())
                        .uuid(optionalPayouts.get().getUuid())
                        .bet(optionalPayouts.get().getBet())
                        .id(optionalPayouts.get().getId())
                        .build());
                payoutsRepoTest.delete(optionalPayouts.get());
                userRepoTest.delete(optionalUser.get());
            } else if (optionalPayouts.isPresent()) {
                transactionExecutionTest.executePayment(PayoutsDto.builder()
                        .availableFunds(optionalPayouts.get().getAvailableFunds())
                        .destinationTag(optionalPayouts.get().getDestinationTag())
                        .account(optionalPayouts.get().getAccount())
                        .payouts(optionalPayouts.get().getPayouts())
                        .tagOut(optionalPayouts.get().getTagOut())
                        .data(optionalPayouts.get().getData())
                        .uuid(optionalPayouts.get().getUuid())
                        .bet(optionalPayouts.get().getBet())
                        .id(optionalPayouts.get().getId())
                        .build());
                payoutsRepoTest.delete(optionalPayouts.get());
            } else optionalUser.ifPresent(user -> userRepoTest.delete(user));

        } else if (commandAnswerDto.getBaseUserUuid().equalsIgnoreCase(StringEnum.DONATION.getValue())) {
            Optional<PayoutsTest> optionalPayouts
                    = payoutsRepoTest.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());

            if (optionalPayouts.isPresent()) {
                transactionExecutionTest.executePayment(PayoutsDto.builder()
                        .availableFunds(optionalPayouts.get().getAvailableFunds())
                        .destinationTag(optionalPayouts.get().getDestinationTag())
                        .payouts(optionalPayouts.get().getPayouts())
                        .account(optionalPayouts.get().getAccount())
                        .tagOut(StringEnum.DONATION.getValue())
                        .data(optionalPayouts.get().getData())
                        .uuid(optionalPayouts.get().getUuid())
                        .bet(optionalPayouts.get().getBet())
                        .id(optionalPayouts.get().getId())
                        .build());
                payoutsRepoTest.delete(optionalPayouts.get());
            }
        }
    }
}
