package com.samuilolegovich.service;

import com.samuilolegovich.domain.Payouts;
import com.samuilolegovich.domain.User;
import com.samuilolegovich.dto.*;
import com.samuilolegovich.enums.*;
import com.samuilolegovich.model.BetLogic;
import com.samuilolegovich.repository.*;
import com.samuilolegovich.service.interfaces.BetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BetServiceImpl implements BetService {
    @Autowired
    private PayoutsRepo payoutsRepo;
    @Autowired
    private LottoRepo lottoRepo;
    @Autowired
    private BetLogic betLogic;
    @Autowired
    private UserRepo userRepo;


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
                        .id(404L)
                        .uuid(InformationAboutRates.PLAYER_NOT_FOUND.getValue())
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


    private CommandAnswerDto getCommandAnswer(UserDto user, BigDecimal pay, BigDecimal lottoNow,
                                              InformationAboutRates enums) {
        StringBuilder stringBuilder = new StringBuilder(lottoNow.toString());

        Payouts payouts = payoutsRepo.save(Payouts.builder()
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
                .uuid(payouts.getUuid())
                .id(payouts.getId())
                .build();
    }

}
