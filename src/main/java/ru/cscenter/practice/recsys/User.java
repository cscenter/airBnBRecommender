package ru.cscenter.practice.recsys;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private final int id;
    private final Language[] languages;
    private final int countReviewsFromHosts;

    private final ArrayList<Integer> visitedFlats = new ArrayList<>();

    public User(final int id,final  Language[] languages, final int countReviewsFromHosts) {
        this.id = id;
        this.languages = languages;
        this.countReviewsFromHosts = countReviewsFromHosts;
    }

    public int getId() {
        return id;
    }

    public Language[] getLanguages() {
        return languages;
    }

    public int getCountReviewsFromHosts() {
        return countReviewsFromHosts;
    }

    public void addVisitedFlat(Integer flatId) {
        if (flatId <= 0)
            throw new IllegalArgumentException("flatId is negative or zero ");

        if (!visitedFlats.contains(flatId))
            visitedFlats.add(flatId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (countReviewsFromHosts != user.countReviewsFromHosts) return false;
        return id == user.id && Arrays.equals(languages, user.languages);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (languages != null ? Arrays.hashCode(languages) : 0);
        result = 31 * result + countReviewsFromHosts;
        return result;
    }



}
