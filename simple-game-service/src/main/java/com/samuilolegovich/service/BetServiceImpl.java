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

import static com.samuilolegovich.enums.Prize.*;
import static com.samuilolegovich.enums.RedBlack.*;
import static com.samuilolegovich.util.LocaleHelper.getLocale;
import static com.samuilolegovich.util.ReadMessageHelper.getMessageForPlayer;

@Service
@AllArgsConstructor
public class BetServiceImpl implements BetService {
    @Autowired
    private ConditionRepo conditionRepo;
    @Autowired
    private ArsenalRepo arsenalRepo;
    @Autowired
    private PayoutsRepo payoutsRepo;
    @Autowired
    private LottoRepo lottoRepo;
    @Autowired
    private BetLogic betLogic;
    @Autowired
    private UserRepo userRepo;

    private final String YOUR_ACCOUNT_IS_NOT_ENOUGH_CREDITS_TO_BET = "there_are_not_enough_credits_in_the_account_for_a_bet";
    private final String INVALID_BET_VALUE_MAXIMUM_RATE = "invalid_bet_value_maximum_rate";
    private final String INVALID_BET_VALUE_LESS_ZERO = "invalid_bet_value_less_zero";
    private final String INVALID_BET_VALUE_ZERO = "invalid_bet_value_zero";
    private final String PLAYER_WIN_SUPER_LOTTO = "player_win_super_lotto";
    private final String NOT_CREDIT_FOR_ANSWER = "not_enough_credit_for_answer";
    private final String SOMETHING_WENT_WRONG = "something_went_wrong";
    private final String PLAYER_NOT_FOUND = "user_is_not_found";
    private final String PLAYER_WIN_LOTTO = "player_win_lotto";
    private final String PLAYER_LOST = "player_lost";
    private final String PLAYER_WIN = "player_win";
    private final String EN = "en";


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
                        .id(0L)
                        .uuid(CommentEnum.PLAYER_NOT_FOUND.toString())
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
