package ru.cscenter.practice.recsys.database;

public interface IAirBnbObjectDao<T> {
    int put(T object);
    T get(int id);
}
