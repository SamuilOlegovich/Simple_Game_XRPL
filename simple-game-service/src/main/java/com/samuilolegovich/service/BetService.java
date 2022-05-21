package com.samuilolegovich.service;

import com.samuilolegovich.domain.Payouts;
import com.samuilolegovich.domain.User;
import com.samuilolegovich.dto.*;
import com.samuilolegovich.enums.*;
import com.samuilolegovich.model.interfaces.Bets;
import com.samuilolegovich.repository.*;
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
@Qualifier("bet-service")
public class BetService implements Bet {
    @Autowired
    private PayoutsRepo payoutsRepo;
    @Autowired
    private LottoRepo lottoRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    @Qualifier("bet-logic")
    private Bets betLogic;


    @Override
    public CommandAnswerDto placeBet(CommandDto command) {
        Optional<User> optionalPlayer = userRepo.findByIdAndUuid(command.getId(), command.getUuid());

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
        BigDecimal lottoNow = lottoRepo.findFirstByOrderByCreatedAtDesc().getTotalLotto();

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
        return betLogic.calculateTheWin(userDto);
    }


    private CommandAnswerDto getCommandAnswer(UserDto userDto, BigDecimal pay, BigDecimal lottoNow,
                                              InformationAboutRates enums) {
        StringBuilder stringBuilder = new StringBuilder(lottoNow.toString());
        stringBuilder.replace(stringBuilder.length() - 6, stringBuilder.length(), "")
                .insert(0, enums.getValue());

        Payouts payouts = payoutsRepo.save(Payouts.builder()
                .destinationTag(userDto.getDestinationTag())
                .availableFunds(userDto.getAvailableFunds())
                .uuid(UUID.randomUUID().toString())
                .tagOut(stringBuilder.toString())
                .userUuid(userDto.getUserUuid())
                .account(userDto.getAccount())
                .userId(userDto.getUserId())
                .data(userDto.getData())
                .bet(userDto.getBet())
                .payouts(pay)
                .build());

        return CommandAnswerDto.builder()
                .baseUserUuid(userDto.getUuid())
                .baseUserId(userDto.getId())
                .uuid(payouts.getUuid())
                .id(payouts.getId())
                .build();
    }

}
