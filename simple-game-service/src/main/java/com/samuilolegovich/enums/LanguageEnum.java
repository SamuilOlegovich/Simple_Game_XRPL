package com.samuilolegovich.enums;

public enum LanguageEnum {
    EN("en"),
    RU("ru"),
    ;

    private String language;
    LanguageEnum(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
