package ru.cscenter.practice.recsys.exceptions;

/**
 * Created by antonkozmirchuk on 20/10/14.
 */
public class TooManyAreasException extends Exception {
    public TooManyAreasException(String msg) {
        super(msg);
    }

    public TooManyAreasException() {
    }
}
