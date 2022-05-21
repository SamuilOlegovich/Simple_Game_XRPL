package com.samuilolegovich.enums;

import lombok.Getter;

public enum ConstantsEnum {
    FOR_INTERNAL_ACCOUNTING(100),
    MAXIMUM_RATE(1000),
    ZERO_BIAS(0),
    ONE_BIAS(1),
    SUPER_LOTTO(42),
    LOTTO(21),
    BOOBY_PRIZE(9),
    DONATE(10),
    PRIZE(90),
    MIDDLE(50),
    STOP(100),
    START(1),
    BIAS(2),
    DONATION(99),
    MINIMUM_LOTTO_FOR_DRAWING_POSSIBILITIES(130000000),
    ;

    @Getter
    private Integer value;

    ConstantsEnum(int value) {
        this.value = value;
    }

}
