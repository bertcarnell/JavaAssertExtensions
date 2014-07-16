/*
 * Copyright (c) 2014 Robert Carnell
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gmail.bertcarnell.assertextensions;

/**
 * Interface to wrap the required logic to:
 * <ol>
 * <li>Execute the statement/s that is/are expected to throw an exception (method performThrowingAction()).</li>
 * <li>Do the corresponding assertions on the exception thrown (method performAssertionsAfterCatch(T)).</li>
 * </ol>
 * We pass this functions into the corresponding static methods in the <code>AssertExtensions</code> class.
 * @author Mariano Navas
 * @author Rob Carnell
 *
 * @param <T> T is a class that extends <code>Throwable</code>
 */
public interface ExceptionAssertionsPerformer<T> {
    /**
     * Method that is expected to throw an exception of type <code>T</code> or assignable from type <code>T</code>
     * @throws java.lang.Throwable
     */
    void performThrowingAction() throws Throwable;

    /**
     * Method used to perform assertions after <code>performThrowingAction</code> has
     * thrown and been caught
     * @param th typically an exception that extends <code>Throwable</code>
     * @throws Exception 
     */
    void performAssertionsAfterCatch(T th) throws Exception;
}
