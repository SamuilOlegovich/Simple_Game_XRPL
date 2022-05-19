package com.samuilolegovich.util;

import com.samuilolegovich.enums.ConstantsEnum;
import com.samuilolegovich.enums.interfaces.Enums;
import com.samuilolegovich.enums.Prize;
import com.samuilolegovich.enums.RedBlack;

public class Converter {

    // конвертирует число в енум
    public static Enums convert(Integer b) {
        if (b == ConstantsEnum.LOTTO.getValue()) return Prize.LOTTO;
        if (b == ConstantsEnum.SUPER_LOTTO.getValue()) return Prize.SUPER_LOTTO;
        if (b > ConstantsEnum.START.getValue() && b < ConstantsEnum.MIDDLE.getValue()) return RedBlack.RED;
        if (b > ConstantsEnum.MIDDLE.getValue() && b < ConstantsEnum.STOP.getValue()) return RedBlack.BLACK;
        return Prize.ZERO;
    }

    public static RedBlack getColorBet(String destinationTag) {
        if (destinationTag.equalsIgnoreCase(RedBlack.RED.getValue())) {
            return RedBlack.RED;
        }
        if (destinationTag.equalsIgnoreCase(RedBlack.BLACK.getValue())) {
            return RedBlack.BLACK;
        }
        if (destinationTag.equalsIgnoreCase(RedBlack.ZERO.getValue())) {
            return RedBlack.ZERO;
        }
        if (destinationTag.equalsIgnoreCase(RedBlack.GET_LOTTO_VOLUME.getValue())) {
            return RedBlack.GET_LOTTO_VOLUME;
        }
        return RedBlack.OTHER;
    }
}
