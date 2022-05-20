package com.samuilolegovich.services;

import com.samuilolegovich.services.interfaces.TransactionExecution;
import com.samuilolegovich.services.interfaces.TransactionPreparation;
import org.springframework.beans.factory.annotation.Autowired;
import com.samuilolegovich.repository.PayoutsRepo;
import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.repository.UserRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.domain.Payouts;
import com.samuilolegovich.dto.PayoutsDto;
import com.samuilolegovich.domain.User;

import lombok.AllArgsConstructor;

import java.util.Optional;

@Service
@AllArgsConstructor
@Qualifier("transaction-preparation-service")
public class TransactionPreparationService implements TransactionPreparation {
    @Autowired
    @Qualifier("transaction-execution-service")
    private TransactionExecution transactionExecutionService;
    @Autowired
    private PayoutsRepo payoutsRepo;
    @Autowired
    private UserRepo userRepo;

    public void prepareTransaction(CommandAnswerDto commandAnswerDto) {
        if (!commandAnswerDto.getUuid().equalsIgnoreCase(StringEnum.PLAYER_NOT_FOUND.getValue())) {
            Optional<Payouts> optionalPayouts
                    = payoutsRepo.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());
            Optional<User> optionalUser
                    = userRepo.findByIdAndUuid(commandAnswerDto.getBaseUserId(), commandAnswerDto.getBaseUserUuid());

            if (optionalPayouts.isPresent() && optionalUser.isPresent()) {
                transactionExecutionService.executePayment(PayoutsDto.builder()
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
                payoutsRepo.delete(optionalPayouts.get());
                userRepo.delete(optionalUser.get());
            }

        } else if (!commandAnswerDto.getUuid().equalsIgnoreCase(StringEnum.DONATION.getValue())) {

            Optional<Payouts> optionalPayouts
                    = payoutsRepo.findByIdAndUuid(commandAnswerDto.getId(), commandAnswerDto.getUuid());

            if (optionalPayouts.isPresent()) {
                transactionExecutionService.executePayment(PayoutsDto.builder()
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
                payoutsRepo.delete(optionalPayouts.get());
            }
        }
    }
}