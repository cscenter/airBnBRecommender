package ru.cscenter.practice.recsys.exceptions;

public class TooManyAreasException extends Exception {
    public TooManyAreasException(String msg) {
        super(msg);
    }

    public TooManyAreasException() {
    }
}
