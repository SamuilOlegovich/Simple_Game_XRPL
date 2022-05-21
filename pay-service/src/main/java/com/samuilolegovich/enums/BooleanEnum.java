package com.samuilolegovich.enums;

import lombok.Getter;

public enum BooleanEnum {
    // Существует ли кошелек или его надо создать
    IS_WALLET(true),
    IS_WALLET_TEST(true),

    // переключатель пойдет реальный или тестовый платеж (при тесте)
    IS_REAL(false),

    // включить и реальный и тестовый солеты -> true -> включены оба
    IS_REAL_AND_TEST_SOCKET(true),
    // если включаем всего один из сокетов то выбираем какой -> true -> реальный
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
