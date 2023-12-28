package com.mygolfclub.utils;

import java.util.function.Consumer;

public class TestsUtils<T> {

    public Exception retrieveException(T toAccept, Consumer<T> consumer) {
        try {
            consumer.accept(toAccept);
            return null;
        } catch (Exception exc) {
            return exc;
        }
    }
}
