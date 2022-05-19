package com.samuilolegovich.enums;

import lombok.Getter;

public enum BooleanEnum {
    IS_WALLET(true),
    IS_WALLET_TEST(true),

    IS_REAL(false),
    IS_REAL_AND_TEST_SOCKET(true),
    IS_REAL_OR_TEST_SOCKET(true),
    ;

    @Getter
    private boolean b;
    BooleanEnum(boolean b) {
        this.b = b;
    }
    private void setValue(boolean b) { this.b = b; }
    public static void setValue(BooleanEnum enums, boolean b) {
        for (BooleanEnum e : BooleanEnum.values()) {
            if (e.equals(enums)) {
                e.setValue(b);
                break;
            }
        }
    }
}
