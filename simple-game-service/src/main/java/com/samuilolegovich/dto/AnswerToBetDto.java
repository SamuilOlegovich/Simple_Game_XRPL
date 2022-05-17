package com.samuilolegovich.dto;

import com.samuilolegovich.enums.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerToBetDto {
    private String userId;
    // выигрышная комбинация
    private Enums winningCombination;
    // заявленная комбинация
    private Enums claimedCombination;
    // информация для ставок
//    private Enums informationForBet;
    // лото сейчас
    private Integer lottoNow;
    // выиграл
    private BigDecimal win;
    private String winnerAddress;
    private String comment;
    private long data;

}
