/*
 * Copyright (c) 2013 Robert Carnell, Mariano Navas
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

import static com.gmail.bertcarnell.assertextensions.AssertExtensions.pass;
import java.beans.Statement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javax.validation.constraints.NotNull;
import static org.junit.Assert.fail;

/**
 * Adds additional Assert methods to the JUnit implementation
 * <p>
 * The AssertThrow methods are necessary for a couple of reasons
 * <ul>
 * <li>The <code>@Test(exception=..)</code> paradigm does not work because it is only looking for at least one statement in a test
 * method to throw. If there are multiple statements in the test method that should throw, then they would have to be broken out
 * into individual test methods
 * <li>The <code>try{statement; fail()}catch(Exception){assertTrue(true)}</code> paradigm works, but is unacceptably long and
 * redundant in the test code.
 * <li>The <code>Exception</code> class also has the same drawbacks as the <code>@Test</code> paradigm.
 * </ul>
 *
 * @author Robert Carnell (bertcarnell@gmail.com)
 * @author Dave Rigsby
 * @author Mariano Navas
 */
public class ExceptionAssertExtensions
{
    /**
     * Prevent instantiation of this class.
     */
    private ExceptionAssertExtensions() {}

    /**
     * Checks that the logic wrapped by the given <code>ExceptionRunnable</code> throws an exception of the specified type 
     *
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param throwerClosure Closure like object that represents the code expected to throw an exception.
     */
    public static <T extends Throwable> void assertThrows(@NotNull Class<T> excType, 
            @NotNull final ExceptionRunnable throwerClosure) {
        assertThrows(excType, throwerClosure, null);
    }

    /**
     * Similar to the other assertThrows methods. Allows the user to pass a custom fail message in case the assertion doesn't pass.
     *
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param throwerClosure Closure like object that represents the code expected to throw an exception.
     * @param customFailMessage Message to throw if the wrong exception is thrown
     */
    public static <T extends Throwable> void assertThrows(Class<T> excType, 
            final ExceptionRunnable throwerClosure, String customFailMessage) {
        ExceptionAssertionsPerformer<T> excAssertsPerformer = new ExceptionAssertionsPerformer<T>() {
            @Override
            public void performThrowingAction() throws Throwable {
                throwerClosure.run();
            }

            @Override
            public void performAssertionsAfterCatch(T th) {}
        };
        assertThrowsAndDoAssertsInCatch(excType, excAssertsPerformer, customFailMessage);
    }

    /**
     * Checks if the given exception type is thrown and perform the given assertions in that exception object.
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param excAssertsPerformer An object that provides methods to perform that will throw and 
     * methods to perform after the catch.
     */
    public static <T extends Throwable> void assertThrowsAndDoAssertsInCatch(Class<T> excType,
            ExceptionAssertionsPerformer<T> excAssertsPerformer)
    {
        assertThrowsAndDoAssertsInCatch(excType, excAssertsPerformer, null);
    }

    /**
     * Similar to the method with the same name. Allows us to customize the error message.
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param excAssertsPerformer An object that provides methods to perform that will throw and
     * methods to perform after the catch.
     * @param customFailMessage Message to throw if the wrong exception is thrown
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void assertThrowsAndDoAssertsInCatch(Class<T> excType,
            ExceptionAssertionsPerformer<T> excAssertsPerformer, String customFailMessage)
    {
        try
        {
            // expect this method to throw
            excAssertsPerformer.performThrowingAction();
            // if it doesn't throw, fail
            fail(createExpectedExceptionMessage(excType, null, customFailMessage));
        }
        // catch if the correct method threw an exception or the fail method threw
        catch (Throwable th)
        {
            // if the expected exception is not assignable from the thrown exception, then something went wrong
            if (!excType.isAssignableFrom(th.getClass()))
            {
                fail(createExpectedExceptionMessage(excType, th.getClass(), customFailMessage));
            }
            // otherwise, perform assertions
            try
            {
                excAssertsPerformer.performAssertionsAfterCatch((T) th);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Assert that a specific type of <code>Throwable</code> is thrown with a specific exception
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param excMessage The expected message attached to the Exception
     * @param throwerClosure Closure like object that represents the code expected to throw an exception.
     */
    public static <T extends Throwable> void assertThrows(@NotNull String excMessage,
            @NotNull Class<T> excType, @NotNull final ExceptionRunnable throwerClosure)
    {
        assertThrows(excMessage, excType, throwerClosure, null);
    }
    
    /**
     * Assert that a specific type of <code>Throwable</code> is thrown with a specific exception, producing a specific message
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param excMessage The expected message attached to the Exception
     * @param throwerClosure Closure like object that represents the code expected to throw an exception.
     * @param customFailMessage A message to be used if the assert fails
     */
    public static <T extends Throwable> void assertThrows(@NotNull String excMessage,
            @NotNull Class<T> excType, @NotNull final ExceptionRunnable throwerClosure,
            String customFailMessage)
    {
        ExceptionAssertionsPerformer<T> eap = new ExceptionAssertionsPerformer<T>() 
        {
            @Override
            public void performThrowingAction() throws Throwable {
                throwerClosure.run();
            }

            @Override
            public void performAssertionsAfterCatch(T th) throws Exception {
                // do nothing
            }
        };
        assertThrowsSpecificException(excMessage, excType, eap, customFailMessage);
    }

    /**
     * Assert that a method in a <code>Runnable</code> closure should throw when tested with a specific message
     *
     * @param excMessage the expected message from the exception
     * @param excType The Class corresponding to the expected exception.
     * @param excAssertsPerformer An object that provides methods to perform that will throw and methods to perform
     *                            after the catch.
     * @param customFailMessage A message to be displayed on failure
     * @param <T> A class that extends Throwable
     */
    @SuppressWarnings("unchecked")
    /**
     * 
     * @param <T> a type that extends <code>Throwable</code>
     * @param excType The Class corresponding to the expected exception.
     * @param excMessage The expected message attached to the Exception
     * @param excAssertsPerformer a class of type <code>ExceptionAssertionsPerformer</code>
     * @param customFailMessage A message to be used if the assert fails
     */
    public static <T extends Throwable> void assertThrowsSpecificException(String excMessage,
            @NotNull Class<T> excType, @NotNull ExceptionAssertionsPerformer<T> excAssertsPerformer,
            String customFailMessage)
    {
        try 
        {
            excAssertsPerformer.performThrowingAction();
            fail(createExpectedExceptionMessage(excType, null, customFailMessage));
        } 
        catch (Throwable th) 
        {
            if (!excType.getName().equals(th.getClass().getName()) ||
                    !th.getMessage().equals(excMessage))
            {
                String msg = String.format("Expected %s with message %s, but was %s with message %s", 
                        excType.getName(), excMessage, th.getClass().getName(),
                        th.getMessage());
                fail(msg);
            }
            try 
            {
                excAssertsPerformer.performAssertionsAfterCatch((T) th);
            } 
            catch (Exception e) 
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * private method to create exception messages
     *
     * @param excType The Class corresponding to the expected exception.
     * @param actualType The Class corresponding to the actual exception
     * @param customFailMessage a custom failure message
     * @return the message
     */
    private static String createExpectedExceptionMessage(Class<? extends Throwable> excType, 
            Class<? extends Throwable> actualType, String customFailMessage)
    {
        String suffix;
        if (actualType != null)
        {
            suffix = String.format(", but was %s", actualType.getName());
        }
        else
        {
            suffix = ", but no exception was thrown";
        }
        String result = String.format("Expected %s%s", excType.getName(), suffix);
        if (customFailMessage != null)
        {
            result += "; " + customFailMessage;
        }
        return result;
    }

    /**
     * Unit test to assert that a specific type of exception is thrown
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li>Assert passes because the right exception is thrown:
     * <ul>
     * <li><code>assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "a");</code>
     * </ul>
     * <li>Assert fails because the wrong exception is thrown:
     * <ul>
     * <li><code>assertThrows(ArithmeticException.class, new Double(0), "parseDouble", "a");</code>
     * </ul>
     * <li>Assert fails because an exception isn't thrown by the method when one is expected
     * <ul>
     * <li><code>assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "1");</code>
     * </ul>
     * </ul>
     *
     * @param expectedException The class of the expected exception type
     * @param target the target object that the method will be called from
     * @param methodName the name of the method that is to be called
     * @param arguments the arguments to be passed to the method
     */
    public static void assertThrows(@NotNull Class<? extends Throwable> expectedException, @NotNull Object target,
            @NotNull String methodName, Object... arguments)
    {
        // create a java.beans.statement which works like Method in reflection
        Statement oStatement = new Statement(target, methodName, arguments);
        try
        {
            oStatement.execute(); // throws Exception
            // if Exception is not thrown
            fail(String.format("Method %s did not throw %s as expected", oStatement.getMethodName(), expectedException.toString()));
        }
        catch (Exception e)
        {
            // the class of the Exception should match the expectedException
            Class<?> temp = e.getClass();
            if (temp != expectedException)
            {
                fail(String.format("Method %s threw %s, but %s was expected", oStatement.getMethodName(), temp.toString(),
                        expectedException.toString()));
            }
            else
            {
                pass();
            }
        }
    }

    /**
     * Unit test to assert that a specific type of exception with a specific message is thrown
     *
     * @see assertThrows
     * @param message The expected message
     * @param expectedException The class of the expected exception type
     * @param target the target object that the method will be called from
     * @param methodName the name of the method that is to be called
     * @param arguments the arguments to be passed to the method
     */
    public static void assertThrows(@NotNull String message, @NotNull Class<? extends Throwable> expectedException, @NotNull Object target,
            @NotNull String methodName, Object... arguments)
    {
        // create a java.beans.statement which works like Method in reflection
        Statement oStatement = new Statement(target, methodName, arguments);
        try
        {
            oStatement.execute(); // throws Exception
            // if Exception is not thrown
            fail(String.format("Method %s did not throw %s as expected", oStatement.getMethodName(), expectedException.toString()));
        }
        catch (Exception e)
        {
            // the class of the Exception should match the expectedException
            Class<?> temp = e.getClass();
            if (temp != expectedException)
            {
                fail(String.format("Method %s threw %s, but %s was expected", oStatement.getMethodName(), temp.toString(),
                        expectedException.toString()));
            }
            else if (!e.getMessage().equals(message))
            {
                fail(String.format("Method %s threw %s, but contained message %s when %s was expected", oStatement.getMethodName(),
                        temp.toString(), e.getMessage(), message));
            }
            else
            {
                pass();
            }
        }
    }

    /**
     * assertThrows for Constructors using reflection
     * <p>
     * <b>Warning:</b> This method cannot tell the difference between constructors when the only difference is a primitive type.
     * For example, it cannot tell the difference between A(double[], Object, double) and A(double[], Object, int)
     * </p>
     *
     * @param expectedException The class of the expected exception type
     * @param constr the target constructor
     * @param arguments the arguments to be passed to the constructor
     */
    public static void assertConstuctorThrows(@NotNull Class<? extends Throwable> expectedException, @NotNull Constructor<?> constr,
            Object... arguments)
    {
        try
        {
            constr.newInstance(arguments);
            fail(String.format("Constructor %s did not throw %s as expected", constr.getName(), expectedException.toString()));
        }
        catch (InstantiationException | InvocationTargetException e)
        {
            if (e.getCause().getClass() != expectedException)
            {
                fail(String.format("Constructor %s threw %s, but %s was expected", constr.getName(), e.getCause().toString(),
                        expectedException.toString()));
            }
            else
            {
                pass();
            }
        }
        catch (IllegalAccessException | IllegalArgumentException e2)
        {
            fail(String.format("Error in invoking constructor %s with arguments %s: %s", constr.getName(), Arrays.toString(arguments),
                    e2.getMessage()));
        }
    }
}
