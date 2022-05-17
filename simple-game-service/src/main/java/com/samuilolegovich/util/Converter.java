package com.samuilolegovich.util;

import com.samuilolegovich.enums.ConstantsEnum;
import com.samuilolegovich.enums.Enums;
import com.samuilolegovich.enums.Prize;
import com.samuilolegovich.enums.RedBlack;

public class Converter {

    // конвертирует число в енум
    public static Enums convert(int b) {
        if (b == ConstantsEnum.LOTTO.getValue()) return Prize.LOTTO;
        if (b == ConstantsEnum.SUPER_LOTTO.getValue()) return Prize.SUPER_LOTTO;
        if (b > ConstantsEnum.START.getValue() && b < ConstantsEnum.MIDDLE.getValue()) return RedBlack.RED;
        if (b > ConstantsEnum.MIDDLE.getValue() && b < ConstantsEnum.STOP.getValue()) return RedBlack.BLACK;
        return Prize.ZERO;
    }

    // конвертируем общее количество монет (кредитов) в базе в удобоваримы вариант для юзера,
    // обыяно делим на 10
    public static long convertForUserCalculation(long creditsPlayer) {
        return (long) (creditsPlayer / ConstantsEnum.FOR_USER_CALCULATIONS.getValue());
    }
}
