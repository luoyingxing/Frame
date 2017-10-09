package com.lyx.frame.annotation;

/**
 * ParserException
 * <p>
 * Created by luoyingxing on 2017/6/2.
 */

public class ParserException extends Exception {

    public ParserException() {
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }
}