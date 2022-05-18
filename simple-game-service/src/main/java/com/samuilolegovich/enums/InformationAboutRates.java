package com.samuilolegovich.enums;

import lombok.Getter;

public enum InformationAboutRates implements Enums {
    NOT_ENOUGH_CREDIT_FOR_ANSWER(""),
    SOMETHING_WENT_WRONG(""),
    INSUFFICIENT_FUNDS(""),
    INCORRECT_RATE(""),

    MAXIMUM_RATE("99"),
    MINIMUM_RATE("11"),
    INFO("13"),

    NOT_CREDIT_FOR_ANSWER("22"),
    ;

    @Getter
    private String value;
    InformationAboutRates(String value) {
        this.value = value;
    }
}
