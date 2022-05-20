package com.samuilolegovich.service;

import com.samuilolegovich.domain.PayoutsTest;
import com.samuilolegovich.domain.UserTest;
import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.CommandDto;
import com.samuilolegovich.dto.UserDto;
import com.samuilolegovich.enums.BigDecimalEnum;
import com.samuilolegovich.enums.InformationAboutRates;
import com.samuilolegovich.model.interfaces.Bets;
import com.samuilolegovich.repository.LottoRepoTest;
import com.samuilolegovich.repository.PayoutsRepoTest;
import com.samuilolegovich.repository.UserRepoTest;
import com.samuilolegovich.service.interfaces.Bet;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Qualifier("bet-service-test")
public class BetTest implements Bet {
    @Autowired
    private PayoutsRepoTest payoutsRepoTest;
    @Autowired
    private LottoRepoTest lottoRepoTest;
    @Autowired
    private UserRepoTest userRepoTest;
    @Autowired
    @Qualifier("bet-logic-test")
    private Bets betLogicTest;


    @Override
    public CommandAnswerDto placeBet(CommandDto command) {
        Optional<UserTest> optionalPlayer = userRepoTest.findByIdAndUuid(command.getId(), command.getUuid());

        return optionalPlayer.map(user -> placeBet(UserDto.builder()
                .destinationTag(user.getDestinationTag())
                .availableFunds(user.getAvailableFunds())
                .account(user.getAccount())
                .uuid(user.getUuid())
                .data(user.getData())
                .bet(user.getBet())
                .id(user.getId())
                .build()))
                .orElse(CommandAnswerDto.builder()
                        .baseUserUuid(InformationAboutRates.PLAYER_NOT_FOUND.getValue())
                        .uuid(InformationAboutRates.PLAYER_NOT_FOUND.getValue())
                        .baseUserId(404L)
                        .id(404L)
                        .build());
    }


    private CommandAnswerDto placeBet(UserDto userDto) {
        BigDecimal lottoNow = lottoRepoTest.findFirstByOrderByCreatedAtDesc().getTotalLotto();

        // ставка выше допустимой
        if (userDto.getBet().compareTo(new BigDecimal(BigDecimalEnum.MAXIMUM_RATE.getValue())) > 0 ) {
            return getCommandAnswer(userDto, new BigDecimal(BigDecimalEnum.INFO_OUT_PAY.getValue()),
                    lottoNow, InformationAboutRates.MAXIMUM_RATE);
        }
        // ставка  ниже допустимой - 0.01XRP
        if (userDto.getBet().compareTo(new BigDecimal(BigDecimalEnum.MAXIMUM_RATE.getValue())) < 0) {
            return getCommandAnswer(userDto, new BigDecimal(BigDecimalEnum.INFO_OUT_PAY.getValue()),
                    lottoNow, InformationAboutRates.MINIMUM_RATE);
        }
        // если недостаточно средств на кошельке для ответной стаки
        if (userDto.getBet().multiply(new BigDecimal(3)).compareTo(userDto.getAvailableFunds()) > 0) {
            return getCommandAnswer(userDto, userDto.getBet(),  lottoNow, InformationAboutRates.NOT_CREDIT_FOR_ANSWER);
        }
        // если все хорошо делаем ставку
        return betLogicTest.calculateTheWin(userDto);
    }


    private CommandAnswerDto getCommandAnswer(UserDto user, BigDecimal pay, BigDecimal lottoNow,
                                              InformationAboutRates enums) {
        StringBuilder stringBuilder = new StringBuilder(lottoNow.toString());

        PayoutsTest payoutsTest = payoutsRepoTest.save(PayoutsTest.builder()
                .destinationTag(user.getDestinationTag())
                .availableFunds(user.getAvailableFunds())
                .uuid(UUID.randomUUID().toString())
                .account(user.getAccount())
                .data(user.getData())
                .bet(user.getBet())
                .payouts(pay)
                .tagOut(stringBuilder
                        .replace(stringBuilder.length() - 6, stringBuilder.length(), "")
                        .insert(0, enums.getValue()).toString())
                .build());

        return CommandAnswerDto.builder()
                .baseUserUuid(user.getUuid())
                .baseUserId(user.getId())
                .uuid(payoutsTest.getUuid())
                .id(payoutsTest.getId())
                .build();
    }
}
