package com.samuilolegovich.enums;

import lombok.Getter;

public enum Prize implements Enums {
    SUPER_LOTTO("42"),
    LOTTO("21"),
    ZERO("66"),
    WIN("77"),
    DONATION("99")
    ;

    @Getter
    private String value;

    Prize(String value) {
        this.value =value;
    }
}
