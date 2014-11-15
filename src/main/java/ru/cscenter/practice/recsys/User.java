package ru.cscenter.practice.recsys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private final int id;
    private final List<Language> languages;
    private final int countReviewsFromHosts;

    private final ArrayList<Integer> visitedFlats = new ArrayList<>();

    public User(final int id, final List<Language> languages, final int countReviewsFromHosts) {
        this.id = id;
        this.languages = languages;
        this.countReviewsFromHosts = countReviewsFromHosts;
    }

    public int getId() {
        return id;
    }

    public List<Language> getLanguages() {
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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", languages=" + languages +
                ", countReviewsFromHosts=" + countReviewsFromHosts +
                ", visitedFlats=" + visitedFlats +
                '}';
    }
}
