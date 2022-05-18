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

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.samuilolegovich.enums.InformationAboutRates.*;
import static com.samuilolegovich.enums.Prize.*;
import static com.samuilolegovich.enums.RedBlack.*;
import static com.samuilolegovich.util.Converter.convertForUserCalculation;
import static com.samuilolegovich.util.LocaleHelper.getLocale;
import static com.samuilolegovich.util.ReadMessageHelper.getMessageForPlayer;

@Service
@AllArgsConstructor
public class BetServiceImpl implements BetService {
    @Autowired
    private ConditionRepo conditionRepo;
    @Autowired
    private DonationsRepo donationsRepo;
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

    private RedBlack getColorBet(String destinationTag) {
        if (destinationTag.equalsIgnoreCase(RED.getValue())) {
            return RED;
        }
        if (destinationTag.equalsIgnoreCase(BLACK.getValue())) {
            return BLACK;
        }
        if (destinationTag.equalsIgnoreCase(RedBlack.ZERO.getValue())) {
            return RedBlack.ZERO;
        }
        if (destinationTag.equalsIgnoreCase(RedBlack.GET_LOTTO_VOLUME.getValue())) {
            return RedBlack.GET_LOTTO_VOLUME;
        }
        return OTHER;
    }


    private CommandAnswerDto placeBet(UserDto user) {
        BigDecimal lottoNow = lottoRepo.findFirstByOrderByCreatedAtDesc().getTotalLotto();

        // недопустимая ставка - больше 100 XRP или меньше 0.1XRP== 0
        if (user.getBet().compareTo(new BigDecimal(BigDecimalEnum.MAXIMUM_RATE.getValue())) > 0
                || user.getBet().compareTo(new BigDecimal(BigDecimalEnum.MAXIMUM_RATE.getValue())) < 0 ) {
            StringBuilder stringBuilder = new StringBuilder(lottoNow.toString());

            Payouts payouts = payoutsRepo.save(Payouts.builder()
                    .destinationTag(user.getDestinationTag())
                    .availableFunds(user.getAvailableFunds())
                    .uuid(UUID.randomUUID().toString())
                    .account(user.getAccount())
                    .payouts(new BigDecimal(BigDecimalEnum.INFO_OUT_PAY.getValue()))
                    .tagOut(stringBuilder
                            .replace(stringBuilder.length() - 6, stringBuilder.length(), "")
                            .insert(0, INFO.getValue()).toString())
                    .data(user.getData())
                    .bet(user.getBet())
                    .build());

            return CommandAnswerDto.builder()
                    .baseUserUuid(user.getUuid())
                    .baseUserId(user.getId())
                    .uuid(payouts.getUuid())
                    .id(payouts.getId())
                    .build();
        }

        // недопустимая ставка < 0
        if (bet < 0) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(INVALID_BET_VALUE_LESS_ZERO, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .informationForBet(INCORRECT_RATE)
                    .claimedCombination(redBlackBet)
                    .lottoNow(lottoNow)
                    .win(0L)
                    .build();
        }

        // ставка выше допустимой
        if (bet > ConstantsEnum.MAXIMUM_RATE.getValue()) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(INVALID_BET_VALUE_MAXIMUM_RATE, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .informationForBet(MAXIMUM_RATE)
                    .claimedCombination(redBlackBet)
                    .lottoNow(lottoNow)
                    .win(0L)
                    .build();
        }

        // если недостаточно кредитов у юзера для ставки
        if (convertForUserCalculation(playerCredits) < (long) bet) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(YOUR_ACCOUNT_IS_NOT_ENOUGH_CREDITS_TO_BET, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .informationForBet(INSUFFICIENT_FUNDS)
                    .claimedCombination(redBlackBet)
                    .lottoNow(lottoNow)
                    .win(0L)
                    .build();
        }

        // если все хорошо делаем ставку
        WonOrNotWon wonOrNotWon = betLogic.calculateTheWin(user, bet, redBlackBet);
        Enums enums = wonOrNotWon.getReplyToBet();

        // обрабатываем ответы по ставке

        // не достаточно кредитов в запасе на ответ ставке
        if (enums.equals(NOT_ENOUGH_CREDIT_FOR_ANSWER)) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(NOT_CREDIT_FOR_ANSWER, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .informationForBet(NOT_ENOUGH_CREDIT_FOR_ANSWER)
                    .claimedCombination(redBlackBet)
                    .lottoNow(lottoNow)
                    .win(0L)
                    .build();
        }

        // В методы ниже вставить код чтобы они отправляли сообщения о выигрыше в общий чат сообщений
        // в формате - никНейм выигарл - сколько

        // выиграл супер лото 42
        if (enums.equals(SUPER_LOTTO)) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(PLAYER_WIN_SUPER_LOTTO, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .claimedCombination(redBlackBet)
                    .winningCombination(SUPER_LOTTO)
                    .informationForBet(SUPER_LOTTO)
                    .win(wonOrNotWon.getWin())
                    .lottoNow(lottoNow)
                    .build();
        }

        // выиграл лото 21
        if (enums.equals(LOTTO)) {
            return AnswerToBetDto.builder()
                    .comment(getMessageForPlayer(PLAYER_WIN_LOTTO, getLocale(EN)))
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .claimedCombination(redBlackBet)
                    .win(wonOrNotWon.getWin())
                    .winningCombination(LOTTO)
                    .informationForBet(LOTTO)
                    .lottoNow(lottoNow)
                    .build();
        }

        // проиграл
        if (enums.equals(ZERO)) {
            return AnswerToBetDto.builder()
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .winningCombination(redBlackBet.equals(RED) ? BLACK : RED)
                    .comment(getMessageForPlayer(PLAYER_LOST, getLocale(EN)))
                    .claimedCombination(redBlackBet)
                    .informationForBet(ZERO)
                    .lottoNow(lottoNow)
                    .win(0L)
                    .build();
        }

        // выиграл
        if (enums.equals(Prize.WIN)) {
            return AnswerToBetDto.builder()
                    .totalPlayerCredits(convertForUserCalculation(playerCredits))
                    .comment(getMessageForPlayer(PLAYER_WIN, getLocale(EN)))
                    .claimedCombination(redBlackBet)
                    .winningCombination(redBlackBet)
                    .win(wonOrNotWon.getWin())
                    .informationForBet(WIN)
                    .lottoNow(lottoNow)
                    .build();
        }

        return AnswerToBetDto.builder()
                .comment(getMessageForPlayer(SOMETHING_WENT_WRONG, getLocale(EN)))
                .informationForBet(InformationAboutRates.SOMETHING_WENT_WRONG)
                .totalPlayerCredits(convertForUserCalculation(playerCredits))
                .claimedCombination(redBlackBet)
                .lottoNow(lottoNow)
                .win(0L)
                .build();
    }
}
