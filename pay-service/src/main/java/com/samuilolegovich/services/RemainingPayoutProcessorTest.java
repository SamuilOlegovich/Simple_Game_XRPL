package com.samuilolegovich.services;

import com.samuilolegovich.domain.PayoutsTest;
import com.samuilolegovich.domain.UserTest;
import com.samuilolegovich.dto.PayoutsDto;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.repository.PayoutsRepoTest;
import com.samuilolegovich.repository.UserRepoTest;
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
@Qualifier("remaining-payout-processor-test")
public class RemainingPayoutProcessorTest implements RemainingPayoutProcessor {
    @Autowired
    @Qualifier("transaction-execution-test")
    private TransactionExecution transactionExecutionTest;
    @Autowired
    private PayoutsRepoTest payoutsRepoTest;
    @Autowired
    private UserRepoTest userRepoTest;

    @Override
    public void handle() {
        List<PayoutsTest> payoutsList = payoutsRepoTest.findAll();
        if (payoutsList != null && payoutsList.size() > 0) {
            payoutsList.forEach(payouts -> {
                if (!payouts.getTagOut().equals(StringEnum.DONATION.getValue())) {
                    transactionExecutionTest.executePayment(PayoutsDto.builder()
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

                    Optional<UserTest> optionalUser
                            = userRepoTest.findByIdAndUuid(payouts.getUserId(), payouts.getUserUuid());
                    optionalUser.ifPresent(user -> userRepoTest.delete(user));
                } else {
                    transactionExecutionTest.executePayment(PayoutsDto.builder()
                            .payouts(payouts.getPayouts())
                            .account(StringEnum.ADDRESS_FOR_SEND_DONATION_TEST.getValue())
                            .tagOut(payouts.getTagOut())
                            .build());
                }
                payoutsRepoTest.delete(payouts);
            });
        }
    }
}
