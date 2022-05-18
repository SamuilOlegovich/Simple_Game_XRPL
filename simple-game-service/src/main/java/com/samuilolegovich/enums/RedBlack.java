package com.samuilolegovich.enums;

import lombok.Getter;

public enum RedBlack implements Enums {
    GET_LOTTO_VOLUME("777"),
    BLACK("666"),
    OTHER("10101"),
    ZERO("10001"),
    RED("999"),
    INFO("13")
    ;

    @Getter
    private String value;
    RedBlack(String value) {
        this.value = value;
    }
}
