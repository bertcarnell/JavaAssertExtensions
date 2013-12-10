package com.gmail.bertcarnell.assertextensions;

/**
 * Interface to wrap the required logic to:
 * 1- Execute the statement/s that is/are expected to throw an exception (method performThrowingAction()).
 * 2- Do the corresponding assertions on the exception thrown (method performAssertionsAfterCatch(T)).
 * We pass this functions into the corresponding static methods in the AssertExtensions class.
 * @author Mariano Navas
 *
 * @param <T>
 */
public interface ExceptionAssertionsPerformer<T> {
    void performThrowingAction();

    void performAssertionsAfterCatch(T th) throws Exception;
}
