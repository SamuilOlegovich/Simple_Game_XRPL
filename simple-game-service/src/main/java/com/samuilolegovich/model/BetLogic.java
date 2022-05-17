package com.samuilolegovich.model;

import com.samuilolegovich.domain.*;
import com.samuilolegovich.dto.WonOrNotWon;
import com.samuilolegovich.enums.*;
import com.samuilolegovich.repository.*;
import com.samuilolegovich.util.Converter;
import com.samuilolegovich.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.samuilolegovich.util.Converter.convertForUserCalculation;

@Component
@RequiredArgsConstructor
public class BetLogic {
    private final ConditionRepo conditionRepo;
    private final DonationsRepo donationsRepo;
    private final ArsenalRepo arsenalRepo;
    private final LottoRepo lottoRepo;
    private final UserRepo userRepo;


    /*  Если будут проблеммы с синхронизацией и неадекватным списанием средств
        то расскоментировать код ниже. А так же внутренности класса BetConfig.

    private static volatile BetLogic bet;


    private BetLogic(ConditionRepo conditionRepo, DonationsRepo donationsRepo, ArsenalRepo arsenalRepo,
                PlayerRepo playerRepo, LottoRepo lottoRepo) {
        this.conditionRepo = conditionRepo;
        this.donationsRepo = donationsRepo;
        this.arsenalRepo = arsenalRepo;
        this.playerRepo = playerRepo;
        this.lottoRepo = lottoRepo;
    }


    public static BetLogic getInstance(ConditionRepo conditionRepo, DonationsRepo donationsRepo, ArsenalRepo arsenalRepo,
                                  PlayerRepo playerRepo, LottoRepo lottoRepo) {
        BetLogic localInstance = bet;
        if (localInstance == null) {
            synchronized (BetLogic.class) {
                localInstance = bet;
                if (localInstance == null) {
                    bet = localInstance = new BetLogic(conditionRepo, donationsRepo, arsenalRepo, playerRepo, lottoRepo);
                }
            }
        }
        return localInstance;
    }
    */




    public WonOrNotWon calculateTheWin(User user, Long bet, RedBlack redBlackBet) {
        // получаем игрока и данные о его кредитах
        long playerCredits = user.getCredits();

        // берем последнюю запись в арсенале (она максимально актуальна на данный момент)
        Arsenal arsenal = arsenalRepo.findFirstByOrderByCreatedAtDesc();
        long arsenalCredit = arsenal.getCredits();

        // проверяем достаточно ли кредитов в запасе на ответ ставке
        if (convertForUserCalculation(arsenalCredit) <= bet) {
            return WonOrNotWon.builder()
                    .totalLottoNow(lottoRepo.findFirstByOrderByCreatedAtDesc().getTotalLottoCredits())
                    .replyToBet(InformationAboutRates.NOT_ENOUGH_CREDIT_FOR_ANSWER)
                    .totalLoansNow(convertForUserCalculation(user.getCredits()))
                    .win(0L)
                    .build();
        }

        // Получаем состояния системы
        Lotto lotto = lottoRepo.findFirstByOrderByCreatedAtDesc();
        Condition condition = conditionRepo.findByBet(bet);

        // получаем данные по состоянию
        long lottoCredits = lotto.getTotalLottoCredits();
        int bias = condition.getBias();

        // генерируем число
        int generatedLotto = Generator.generate();

        // если смещение больше нуля то проверяем на выигрыш
        if (bias > ConstantsEnum.ZERO_BIAS.getValue()) {
            // если лото позволяет дробление
            if (checkForWinningsLotto(lottoCredits)) {
                if (generatedLotto == ConstantsEnum.LOTTO.getValue())
                    return point(user, playerCredits, lottoCredits);
                if (generatedLotto == ConstantsEnum.SUPER_LOTTO.getValue())
                    return superLotto(user, playerCredits, lottoCredits);
            }

            return takeIntoAccountTheBias(user, playerCredits, bet, redBlackBet, arsenalCredit,
                    lottoCredits, condition, bias);
        }

        return wonOrNotWon(user, playerCredits, bet, redBlackBet, generatedLotto,
                arsenalCredit, lottoCredits, condition);
    }





    private WonOrNotWon point(User user, long playerCredits, long lottoCredits) {
        long onePercent = lottoCredits / 100L;
        long boobyPrize = onePercent * ConstantsEnum.BOOBY_PRIZE.getValue();
        long totalDonation = donationsRepo.findFirstByOrderByCreatedAtDesc().getTotalDonations() + onePercent;

        user.setCredits(playerCredits + boobyPrize);

        lottoRepo.save(Lotto.builder()
                .totalLottoCredits(lottoCredits - (boobyPrize + onePercent))
                .build());

        donationsRepo.save(Donations.builder()
                .totalDonations(totalDonation)
                .donations(onePercent)
                .typeWin(Prize.LOTTO)
                .build());

        userRepo.save(user);

        return WonOrNotWon.builder()
                .totalLoansNow(convertForUserCalculation(user.getCredits()))
                .win(convertForUserCalculation(boobyPrize))
                .totalLottoNow(lottoCredits)
                .replyToBet(Prize.LOTTO)
                .build();
    }



    private WonOrNotWon superLotto(User user, long playerCredits, long lottoCredits) {
        // добавить откусывание 10 процентов в фонд
        long onePercent = lottoCredits / 100L;
        long donation = onePercent * ConstantsEnum.DONATE.getValue();
        long allLotto = onePercent * ConstantsEnum.PRIZE.getValue();
        long totalDonations = donationsRepo.findFirstByOrderByCreatedAtDesc().getTotalDonations() + donation;

        user.setCredits(playerCredits + allLotto);

        donationsRepo.save(Donations.builder()
                .totalDonations(totalDonations)
                .typeWin(Prize.LOTTO)
                .donations(donation)
                .build());

        lottoRepo.save(Lotto.builder()
                .totalLottoCredits(0L)
                .build());

        userRepo.save(user);

        return WonOrNotWon.builder()
                .totalLoansNow(convertForUserCalculation(user.getCredits()))
                .win(convertForUserCalculation(allLotto))
                .replyToBet(Prize.SUPER_LOTTO)
                .totalLottoNow(lottoCredits)
                .build();
    }



    private WonOrNotWon takeIntoAccountTheBias(User user, long playerCredits, Long bet,
                                               RedBlack redBlackBet, long arsenalCredit, long lottoCredits,
                                               Condition condition, int bias) {

        Long resultCredits = bet * ConstantsEnum.FOR_USER_CALCULATIONS.getValue();

        user.setCredits(playerCredits - resultCredits);

        // перенос средств в лото или арсенал
        if (bias == ConstantsEnum.ONE_BIAS.getValue()) {
            lottoRepo.save(Lotto.builder()
                    .totalLottoCredits(lottoCredits + resultCredits)
                    .build());
        } else {
            arsenalRepo.save(Arsenal.builder()
                    .credits(arsenalCredit + resultCredits)
                    .build());
        }

        // уменьшаем смещение
        condition.setBias(bias - 1);

        conditionRepo.save(condition);
        userRepo.save(user);

        return WonOrNotWon.builder()
                .totalLottoNow(lottoCredits)
                .replyToBet(Prize.ZERO)
                .win(0L)
                .build();
    }



    private WonOrNotWon wonOrNotWon(User user, long playerCredits, Long bet, RedBlack redBlackBet,
                                    int generatedLotto, long arsenalCredits, long lottoCredits, Condition condition) {

        // если лото позволяет дробление
        if (checkForWinningsLotto(lottoCredits)) {
            if (generatedLotto == ConstantsEnum.LOTTO.getValue())
                return point(user, playerCredits, lottoCredits);
            if (generatedLotto == ConstantsEnum.SUPER_LOTTO.getValue())
                return superLotto(user, playerCredits, lottoCredits);
        }

        Enums redBlackConvert = Converter.convert(generatedLotto);

        Long resultCredits = bet * ConstantsEnum.FOR_USER_CALCULATIONS.getValue();

        // если игрок выиграл
        if (redBlackConvert.equals(redBlackBet)) {
            condition.setBias(ConstantsEnum.BIAS.getValue());
            user.setCredits(playerCredits + resultCredits);

            arsenalRepo.save(Arsenal.builder()
                    .credits(arsenalCredits - resultCredits)
                    .build());

            conditionRepo.save(condition);
            userRepo.save(user);

            return WonOrNotWon.builder()
                    .totalLottoNow(lottoCredits)
                    .replyToBet(Prize.WIN)
                    .win(bet)
                    .build();
        }

        user.setCredits(playerCredits - resultCredits);

        lottoRepo.save(Lotto.builder()
                .totalLottoCredits(lottoCredits + resultCredits)
                .build());

        userRepo.save(user);

        return WonOrNotWon.builder()
                .totalLoansNow(convertForUserCalculation(user.getCredits()))
                .totalLottoNow(lottoCredits)
                .replyToBet(Prize.ZERO)
                .win(0L)
                .build();
    }



    private boolean checkForWinningsLotto(long lottoCredits) {
        return lottoCredits >= ConstantsEnum.MINIMUM_LOTTO_FOR_DRAWING_POSSIBILITIES.getValue();
    }
}
