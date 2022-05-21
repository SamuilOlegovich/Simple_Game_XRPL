package com.samuilolegovich.services;

import com.samuilolegovich.domain.Payouts;
import com.samuilolegovich.domain.User;
import com.samuilolegovich.dto.PayoutsDto;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.repository.PayoutsRepo;
import com.samuilolegovich.repository.UserRepo;
import com.samuilolegovich.services.interfaces.RemainingPayoutProcessor;
import com.samuilolegovich.services.interfaces.TransactionExecution;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Qualifier("remaining-payout-processor-service")
public class RemainingPayoutProcessorService implements RemainingPayoutProcessor {
    @Autowired
    @Qualifier("transaction-execution-service")
    private TransactionExecution transactionExecution;
    @Autowired
    private PayoutsRepo payoutsRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public void handle() {
        List<Payouts> payoutsList = payoutsRepo.findAll();
        if (payoutsList != null && payoutsList.size() > 0) {
            payoutsList.forEach(payouts -> {
                if (!payouts.getTagOut().equals(StringEnum.DONATION.getValue())) {
                    transactionExecution.executePayment(PayoutsDto.builder()
                            .destinationTag(payouts.getDestinationTag())
                            .availableFunds(payouts.getAvailableFunds())
                            .payouts(payouts.getPayouts())
                            .account(payouts.getAccount())
                            .tagOut(payouts.getTagOut())
                            .data(payouts.getData())
                            .uuid(payouts.getUuid())
                            .bet(payouts.getBet())
                            .id(payouts.getId())
                            .build());
                    Optional<User> optionalUser = userRepo.findByIdAndUuid(payouts.getUserId(), payouts.getUserUuid());
                    optionalUser.ifPresent(user -> userRepo.delete(user));
                } else {
                    transactionExecution.executePayment(PayoutsDto.builder()
                            .payouts(payouts.getPayouts())
                            .account(payouts.getAccount())
                            .tagOut(payouts.getTagOut())
                            .build());
                }
                payoutsRepo.delete(payouts);
            });
        }
    }
}
