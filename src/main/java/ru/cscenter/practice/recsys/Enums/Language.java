package ru.cscenter.practice.recsys.Enums;


public enum Language {
    ENGLISH("English"),
    RUSSIAN("Russian"),
    FRENCH("French"),
    DEUTSCH("Deutsch"),
    CHINEESE("Chineese"),
    JAPANEESE("Japaneese"),
    SPAINISH("Spanish"),
    ITALIANO("Italiano");

    private final String lang;

    Language(String lang) {
        this.lang = lang;
    }

    public String getLanguage() {
        return lang;
    }

    public static Language getLanguage(String language) {
        for (Language currentLang : Language.values())
            if (language.equals(currentLang.getLanguage()))
                return currentLang;
        return null;
    }

}
