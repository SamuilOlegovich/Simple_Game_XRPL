package com.samuilolegovich.enums;

public enum CommentEnum {
    YOUR_ACCOUNT_IS_NOT_ENOUGH_CREDITS_TO_BET("there_are_not_enough_credits_in_the_account_for_a_bet"),
    INVALID_BET_VALUE_MAXIMUM_RATE("invalid_bet_value_maximum_rate"),
    INVALID_BET_VALUE_LESS_ZERO("invalid_bet_value_less_zero"),
    INVALID_BET_VALUE_ZERO("invalid_bet_value_zero"),
    PLAYER_WIN_SUPER_LOTTO("player_win_super_lotto"),
    NOT_CREDIT_FOR_ANSWER("not_enough_credit_for_answer"),
    SOMETHING_WENT_WRONG("something_went_wrong"),
    PLAYER_NOT_FOUND("user_is_not_found"),
    PLAYER_WIN_LOTTO("player_win_lotto"),
    PLAYER_LOST("player_lost"),
    PLAYER_WIN("player_win"),

    ;

    private String value;

    CommentEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
