package com.gmail.bertcarnell.assertextensions;

/**
 * Interface to wrap the required logic to:
 * 1- Execute the statement/s that is/are expected to throw an exception (method performThrowingAction()).
 * 2- Do the corresponding assertions on the exception thrown (method performAssertionsAfterCatch(T)).
 * We pass this functions into the corresponding static methods in the AssertExtensions class.
 * @author Mariano Navas
 *
 * @param <T>
 *     T is a class that extends Throwable in practice
 */
public interface ExceptionAssertionsPerformer<T> {
    /**
     * Method that is expected to throw an exception of type T or assignable from type T
     */
    void performThrowingAction();

    /**
     * Method used to perform assertions after <code>performThrowingAction</code> has
     * thrown and been caught
     * @param th typically an expection that extends throwable
     * @throws Exception 
     */
    void performAssertionsAfterCatch(T th) throws Exception;
}
