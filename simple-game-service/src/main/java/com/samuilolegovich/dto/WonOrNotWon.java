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
public class WonOrNotWon {
    // заявленная ставка
    private Enums declaredRate;
    // ответ на ставку
    private Enums replyToBet;
    // ставка
    private BigDecimal bet;
    // выиграш
    private BigDecimal win;
    // объем лото сейчас
    private BigDecimal totalLottoNow;
}
