package ru.cscenter.practice.recsys.database;

import java.util.ArrayList;
import java.util.List;

public class Pair<A, B> {

    private final A fst;
    private final B snd;

    private Pair(A a, B b) {
        this.fst = a;
        this.snd = b;
    }


    public A fst() {
        return fst;
    }

    public B snd() {
        return snd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (fst != null ? !fst.equals(pair.fst) : pair.fst != null) return false;
        return !(snd != null ? !snd.equals(pair.snd) : pair.snd != null);

    }

    @Override
    public int hashCode() {
        int result = fst != null ? fst.hashCode() : 0;
        result = 31 * result + (snd != null ? snd.hashCode() : 0);
        return result;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair(a, b);
    }

    public static List<Pair<Integer, Integer>> makeArrayOfPair(final List<Integer> arrayOfUsers, final Integer flatId) {
        final List<Pair<Integer, Integer>> result = new ArrayList<>();

        for (Integer currentUserIdFromComment : arrayOfUsers)
            result.add(Pair.of(currentUserIdFromComment, flatId));
        return result;
    }

}