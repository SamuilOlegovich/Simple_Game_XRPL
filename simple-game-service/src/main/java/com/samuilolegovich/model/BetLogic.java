package com.samuilolegovich.model;

import com.samuilolegovich.domain.*;
import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.UserDto;
import com.samuilolegovich.enums.*;
import com.samuilolegovich.model.interfaces.Bets;
import com.samuilolegovich.repository.*;
import com.samuilolegovich.util.Converter;
import com.samuilolegovich.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


@Component
@Qualifier("bet-logic")
@RequiredArgsConstructor
public class BetLogic implements Bets {
    private final ConditionRepo conditionRepo;
    private final RabbitTemplate template;
    private final PayoutsRepo payoutsRepo;
    private final LottoRepo lottoRepo;


    @Override
    public CommandAnswerDto calculateTheWin(UserDto userDto) {
        // Получаем состояния системы
        Lotto lotto = lottoRepo.findFirstByOrderByCreatedAtDesc();
        Optional<Condition> conditionOptional = conditionRepo.findByBet(roundTheBet(userDto.getBet()));
        Condition condition;

        if (conditionOptional.isEmpty()) {
            condition = conditionRepo.save(Condition.builder()
                    .bet(roundTheBet(userDto.getBet()))
                    .bias(0)
                    .build());
        } else {
            condition = conditionOptional.get();
        }

        // получаем данные по состоянию
        BigDecimal lottoCredits = lotto.getTotalLotto();
        Integer bias = condition.getBias();

        // генерируем число
        Integer generatedLotto = Generator.generate();

        // если смещение больше нуля то проверяем на выигрыш
        if (bias > ConstantsEnum.ZERO_BIAS.getValue()) {
            // если лото позволяет дробление
            if (checkForWinningsLotto(lottoCredits)) {
                if (generatedLotto == ConstantsEnum.LOTTO.getValue()) {
                    return point(userDto, lottoCredits);
                }
                if (generatedLotto == ConstantsEnum.SUPER_LOTTO.getValue()) {
                    return superLotto(userDto, lottoCredits);
                }
            }
            return takeIntoAccountTheBias(userDto, lottoCredits, condition, bias);
        }
        return wonOrNotWon(userDto, generatedLotto, lottoCredits, condition);
    }





    private CommandAnswerDto point(UserDto userDto, BigDecimal lottoCredits) {
        BigDecimal realLotto = lottoCredits.subtract(new BigDecimal(BigDecimalEnum.ONE_XRP.getValue()));
        BigDecimal onePercent = realLotto.divide(new BigDecimal(BigDecimalEnum.ONE_PERCENT.getValue()));
        BigDecimal boobyPrize = onePercent.multiply(new BigDecimal(ConstantsEnum.BOOBY_PRIZE.getValue()));
        BigDecimal lottoNow = lottoCredits.subtract(boobyPrize.add(onePercent));

        lottoRepo.save(Lotto.builder().totalLotto(lottoNow).build());
        sendDonationToTheOwner(userDto, onePercent, Prize.LOTTO);

        return sendPayment(userDto, lottoNow, boobyPrize, Prize.LOTTO);
    }

    private CommandAnswerDto superLotto(UserDto userDto, BigDecimal lottoCredits) {
        // добавить откусывание 10 процентов в фонд
        BigDecimal realLotto = lottoCredits.subtract(new BigDecimal(BigDecimalEnum.ONE_XRP.getValue()));
        BigDecimal onePercent = realLotto.divide(new BigDecimal(BigDecimalEnum.ONE_PERCENT.getValue()));
        BigDecimal donation = onePercent.multiply(new BigDecimal(ConstantsEnum.DONATE.getValue()));
        BigDecimal allLotto = onePercent.multiply(new BigDecimal(ConstantsEnum.PRIZE.getValue()));
        BigDecimal lottoNow = lottoCredits.subtract(allLotto.add(donation));

        lottoRepo.save(Lotto.builder().totalLotto(lottoNow).build());
        sendDonationToTheOwner(userDto, donation, Prize.SUPER_LOTTO);

        return sendPayment(userDto, lottoNow, allLotto, Prize.SUPER_LOTTO);
    }



    private CommandAnswerDto takeIntoAccountTheBias(UserDto userDto, BigDecimal lottoCredits, Condition condition,
                                                    Integer bias) {
        BigDecimal roundTheBet = roundTheBet(userDto.getBet());
        BigDecimal lottoNow = lottoCredits;

        // перенос средств в лото или арсенал
        if (bias == ConstantsEnum.ONE_BIAS.getValue()) {
            Lotto lotto = lottoRepo.save(Lotto.builder().totalLotto(lottoCredits.add(roundTheBet)).build());
            lottoNow = lotto.getTotalLotto();
        }
        // уменьшаем смещение
        condition.setBias(bias - 1);
        conditionRepo.save(condition);
        return sendPayment(userDto, lottoNow, new BigDecimal(BigDecimalEnum.INFO_OUT_PAY.getValue()), Prize.ZERO);
    }



    private CommandAnswerDto wonOrNotWon(UserDto userDto, Integer generatedLotto, BigDecimal lottoCredits,
                                         Condition condition) {
        // если лото позволяет дробление
        if (checkForWinningsLotto(lottoCredits)) {
            if (generatedLotto == ConstantsEnum.LOTTO.getValue())
                return point(userDto, lottoCredits);
            if (generatedLotto == ConstantsEnum.SUPER_LOTTO.getValue())
                return superLotto(userDto, lottoCredits);
        }

        // если игрок выиграл
        if (Converter.convert(generatedLotto).equals(Converter.getColorBet(userDto.getDestinationTag()))) {
            condition.setBias(ConstantsEnum.BIAS.getValue());
            conditionRepo.save(condition);
            return sendPayment(userDto, lottoCredits, roundTheBet(userDto.getBet()).add(roundTheBet(userDto.getBet())),
                    Prize.WIN);
        }

        lottoRepo.save(Lotto.builder().totalLotto(lottoCredits.add(userDto.getBet())).build());
        return sendPayment(userDto, lottoCredits, new BigDecimal(BigDecimalEnum.INFO_OUT_PAY.getValue()), Prize.ZERO);
    }




    private void sendDonationToTheOwner(UserDto userDto, BigDecimal donation, Prize prize) {
        Payouts payouts = payoutsRepo.save(Payouts.builder()
                .tagOut(Prize.DONATION.getValue() + prize.getValue())
                .account(StringEnum.DONATION_ADDRESS.getValue())
                .destinationTag(Prize.DONATION.getValue())
                .uuid(UUID.randomUUID().toString())
                .data(Prize.DONATION.getValue())
                .userUuid(userDto.getUserUuid())
                .userId(userDto.getUserId())
                .availableFunds(donation)
                .payouts(donation)
                .bet(donation)
                .build());

        template.convertAndSend(StringEnum.MAKE_PAYMENT_ROUTING_KEY.getValue(), CommandAnswerDto.builder()
                .baseUserId((long) ConstantsEnum.DONATION.getValue())
                .baseUserUuid(Prize.DONATION.getValue())
                .uuid(payouts.getUuid())
                .id(payouts.getId())
                .build());
    }

    private CommandAnswerDto sendPayment(UserDto userDto, BigDecimal lottoCredits, BigDecimal pay, Prize prize) {
        StringBuilder stringBuilder = new StringBuilder(lottoCredits.toString());
        stringBuilder.replace(stringBuilder.length() - 6, stringBuilder.length(), "")
                .insert(0, prize.getValue());

        Payouts payouts = payoutsRepo.save(Payouts.builder()
                .destinationTag(userDto.getDestinationTag())
                .availableFunds(userDto.getAvailableFunds())
                .uuid(UUID.randomUUID().toString())
                .tagOut(stringBuilder.toString())
                .userUuid(userDto.getUserUuid())
                .account(userDto.getAccount())
                .userId(userDto.getUserId())
                .payouts(roundTheBet(pay))
                .data(userDto.getData())
                .bet(userDto.getBet())
                .build());

        return CommandAnswerDto.builder()
                .baseUserUuid(userDto.getUuid())
                .baseUserId(userDto.getId())
                .uuid(payouts.getUuid())
                .id(payouts.getId())
                .build();
    }

    private boolean checkForWinningsLotto(BigDecimal lottoCredits) {
        return lottoCredits
                .compareTo(new BigDecimal(ConstantsEnum.MINIMUM_LOTTO_FOR_DRAWING_POSSIBILITIES.getValue())) >= 0;
    }

    private BigDecimal roundTheBet(BigDecimal bet) {
        StringBuilder stringBuilder = new StringBuilder(bet.toString());
        if (stringBuilder.length() > 4) {
            stringBuilder.replace(stringBuilder.length() - 4, stringBuilder.length(), "0000");
        }
        return new BigDecimal(stringBuilder.toString());
    }
}
