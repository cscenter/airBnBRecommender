package ru.cscenter.practice.recsys;

import java.util.ArrayList;

public class Language {

    private final String lang;

    private final static ArrayList<Language> instances = new ArrayList<>();

    private Language(final String lang) {
        this.lang = lang;
    }

    public static Language getInstance(final String lang) {
        for (Language currentLanguage : instances) {
            if (currentLanguage.getLanguage().equals(lang)) {
                return currentLanguage;
            }
        }

        instances.add(new Language(lang));
        return instances.get(instances.size() - 1); // return last
    }

    public String getLanguage() {
        return lang;
    }

    @Override
    public String toString() {
        return lang;
    }
}