package com.samuilolegovich.enums;

import lombok.Getter;

public enum BigDecimalEnum {
    MAXIMUM_RATE("100000000"),
    MINIMUM_RATE("100000"),
    INFO_OUT_PAY("1"),
    ;

    @Getter
    private String value;
    BigDecimalEnum(String value) {
        this.value = value;
    }
}
