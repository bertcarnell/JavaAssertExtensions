package com.gmail.bertcarnell.assertextensions;

public interface ExceptionAssertionsPerformer<T> {
    void performThrowingAction();

    void performAssertionsAfterCatch(T th) throws Exception;
}
