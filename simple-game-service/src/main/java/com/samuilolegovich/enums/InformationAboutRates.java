package com.samuilolegovich.enums;

import com.samuilolegovich.enums.interfaces.Enums;
import lombok.Getter;

public enum InformationAboutRates implements Enums {
    NOT_CREDIT_FOR_ANSWER("22"),
    PLAYER_NOT_FOUND("404"),
    MAXIMUM_RATE("99"),
    MINIMUM_RATE("11"),
    INFO("13"),
    ;

    @Getter
    private String value;
    InformationAboutRates(String value) {
        this.value = value;
    }
}
