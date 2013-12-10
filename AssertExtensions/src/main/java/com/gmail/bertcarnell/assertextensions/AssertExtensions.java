/*
 * Copyright 2013 Robert Carnell
 */
package com.gmail.bertcarnell.assertextensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.Statement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * Adds additional Assert methods to the JUnit implementation
 * <p>The AssertThrow methods are necessary for a couple of reasons
 * <ul>
 *   <li> The <code>@Test(exception=..)</code> paradigm does not work because it is only
 *        looking for at least one statement in a test method to throw.  If there
 *        are multiple statements in the test method that should throw, then they
 *        would have to be broken out into individual test methods
 *   <li> The <code>try{statement; fail()}catch(Exception){assertTrue(true)}</code> paradigm works,
 *        but is unacceptably long and redundant in the test code.
 *   <li> The <code>Exception</code> class also has the same drawbacks as the <code>@Test</code> paradigm.
 * </ul>
 * @author Robert Carnell (bertcarnell@gmail.com)
 * @author Dave Rigsby
 * @author Mariano Navas (marianudo@gmail.com)
 */
public class AssertExtensions
{
    /**
     * Prevent instantiation of this class.
     */
    private AssertExtensions() {}

    public static <T extends Throwable> void assertThrows(Class<T> excType, final Runnable throwerClosure) {
        assertThrows(excType, throwerClosure, null);
    }

    public static <T extends Throwable> void assertThrows(Class<T> excType, final Runnable throwerClosure,
            String customFailMessage) {
        ExceptionAssertionsPerformer<T> excAssertsPerformer = new ExceptionAssertionsPerformer<T>() {
            @Override
            public void performThrowingAction() {
                throwerClosure.run();
            }

            @Override
            public void performAssertionsAfterCatch(T th) {
                // Do nothing
            }
        };
        asserThrowsAndDoAssertsInCatch(excType, excAssertsPerformer, customFailMessage);
    }

    public static <T extends Throwable> void asserThrowsAndDoAssertsInCatch(Class<T> excType,
            ExceptionAssertionsPerformer<T> excAssertsPerformer) {
        asserThrowsAndDoAssertsInCatch(excType, excAssertsPerformer, null);
    }

    public static void assertSetsEqualsAsLists(Set<?> expected, Set<?> actual) {
        assertEquals(new ArrayList<Object>(expected), new ArrayList<Object>(actual));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void asserThrowsAndDoAssertsInCatch(Class<T> excType,
            ExceptionAssertionsPerformer<T> excAssertsPerformer, String customFailMessage) {
        try {
            excAssertsPerformer.performThrowingAction();
            fail(createExpectedExceptionMessage(excType, null, customFailMessage));
        } catch (Throwable th) {
            if (!excType.isAssignableFrom(th.getClass())) {
                fail(createExpectedExceptionMessage(excType, th.getClass(), customFailMessage));
            }
            try {
                excAssertsPerformer.performAssertionsAfterCatch((T) th);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String createExpectedExceptionMessage(Class<? extends Throwable> excType,
            Class<? extends Throwable> actualType, String customFailMessage) {
        String suffix;
        if (actualType != null) {
            suffix = String.format(", but was %s", actualType.getName());
        } else {
            suffix = ", but no exception was thrown";
        }
        String result = String.format("Expected %s%s", excType.getName(), suffix);
        if (customFailMessage != null) {
            result += "; " + customFailMessage;
        }
        return result;
    }

    /**
     * Alternate method of assertThrows using Methods and reflection
     * @deprecated Is not robust to overloaded methods
     * @param expectedException
     * @param target
     * @param methodName
     * @param arguments
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Deprecated
    public static void assertThrowsAlternate(Class<? extends Throwable> expectedException, Object target, String methodName, Object... arguments)
            throws IllegalAccessException, InvocationTargetException
    {
        Method[] methods = target.getClass().getMethods();
        Method method = null;
        int matchingMethodCount = 0;
        for (Method m : methods)
        {
            if (m.getName().equals(methodName))
            {
                method = m;
                matchingMethodCount++;
            }
        }
        if (method != null && matchingMethodCount == 1)
        {
            assertThrowsAlternate(expectedException, target, method, arguments);
        }
        else if (matchingMethodCount > 1) {
            throw new IllegalArgumentException(String.format("Method %s is overloaded.  Use the alternate AssertThrows", methodName));
        }
        else {
            throw new IllegalArgumentException(String.format("Method %s not found on %s", methodName, target.getClass().toString()));
        }
    }

    /**
     * Alternate Method of assertThrows using Methods
     * @deprecated requires too much setup during testing
     * @param expectedException
     * @param target
     * @param method
     * @param arguments
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Deprecated
    public static void assertThrowsAlternate(Class<? extends Throwable> expectedException, Object target, Method method, Object[] arguments)
            throws IllegalAccessException, InvocationTargetException
    {
        try
        {
            method.invoke(target, arguments);
            fail(String.format("Method %s did not throw %s as expected", method.getName(), expectedException.toString()));
        }
        // e will be an InvocationTargetException with inner Exception of the type that we are looking for
        catch (InvocationTargetException e)
        {
            if (e.getCause().getClass() != expectedException) {
                fail(String.format("Method %s threw %s, but %s was expected", method.getName(), e.getCause().toString(), expectedException.toString()));
            }
            else {
                pass();
            }
        }
        catch (IllegalAccessException | IllegalArgumentException e2)
        {
            fail(String.format("Error in invoking method %s on target %s with arguments %s: %s", method.getName(), target.toString(), Arrays.toString(arguments), e2.getMessage()));
        }
    }

    /**
     * Unit test to assert that a specific type of exception is thrown
     * <p> Examples:</p>
     * <ul>
     *   <li>Assert passes because the right exception is thrown:
     *   <ul>
     *     <li><code>assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "a");</code>
     *   </ul>
     *   <li>Assert fails because the wrong exception is thrown:
     *   <ul>
     *     <li><code>assertThrows(ArithmeticException.class, new Double(0), "parseDouble", "a");</code>
     *   </ul>
     *   <li>Assert fails because an exception isn't thrown by the method when one is expected
     *   <ul>
     *     <li><code>assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "1");</code>
     *   </ul>
     * </ul>
     * @param expectedException The class of the expected exception type
     * @param target the target object that the method will be called from
     * @param methodName the name of the method that is to be called
     * @param arguments the arguments to be passed to the method
     */
    public static void assertThrows(@NotNull Class<? extends Throwable> expectedException, @NotNull Object target, @NotNull String methodName, Object... arguments)
    {
        // create a java.beans.statement which works like Method in refelction
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
            if (temp != expectedException) {
                fail(String.format("Method %s threw %s, but %s was expected", oStatement.getMethodName(), temp.toString(), expectedException.toString()));
            }
            else {
                pass();
            }
        }
    }

    /**
     * Unit test to assert that a specific type of exception with a specific message is thrown
     * @see assertThrows
     * @param message The expected message
     * @param expectedException The class of the expected exception type
     * @param target the target object that the method will be called from
     * @param methodName the name of the method that is to be called
     * @param arguments the arguments to be passed to the method
     */
    public static void assertThrows(@NotNull String message, @NotNull Class<? extends Throwable> expectedException, @NotNull Object target, @NotNull String methodName, Object... arguments)
    {
        // create a java.beans.statement which works like Method in refelction
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
            if (temp != expectedException) {
                fail(String.format("Method %s threw %s, but %s was expected", oStatement.getMethodName(), temp.toString(), expectedException.toString()));
            }
            else if (!e.getMessage().equals(message)) {
                fail(String.format("Method %s threw %s, but contained message %s when %s was expected", oStatement.getMethodName(), temp.toString(), e.getMessage(), message));
            }
            else {
                pass();
            }
        }
    }

    /**
     * assertThrows for Constructors using reflection
     * <p><b>Warning:</b>  This method cannot tell the difference between constructors
     * when the only difference is a primitive type.  For example, it cannot tell
     * the difference between A(double[], Object, double) and A(double[], Object, int)
     * @param expectedException The class of the expected exception type
     * @param constr the target constructor
     * @param arguments the arguments to be passed to the constructor
     */
    public static void assertConstuctorThrows(@NotNull Class<? extends Throwable> expectedException, @NotNull Constructor<?> constr, Object... arguments)
    {
        try
        {
            constr.newInstance(arguments);
            fail(String.format("Constructor %s did not throw %s as expected", constr.getName()));
        }
        catch (InstantiationException | InvocationTargetException e)
        {
            if (e.getCause().getClass() != expectedException) {
                fail(String.format("Constructor %s threw %s, but %s was expected", constr.getName(), e.getCause().toString(), expectedException.toString()));
            }
            else {
                pass();
            }
        }
        catch (IllegalAccessException | IllegalArgumentException e2)
        {
            fail(String.format("Error in invoking constructor %s with arguments %s: %s", constr.getName(), Arrays.toString(arguments), e2.getMessage()));
        }
    }
    /**
     * Extension shorthand for <code>assertTrue(true)</code> to mirror <code>fail()</code>
     */
    public static void pass()
    {
        assertTrue(true);
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error.
     * Log relative error measures the number of significant digits of agreement.
     * @param expected
     * @param actual
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(double expected, double actual, int lre)
    {
        assertEqualsLRE("", expected, actual, lre);
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error.
     * Log relative error measures the number of significant digits of agreement.
     * @param message message if the test fails
     * @param expected
     * @param actual
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(@NotNull String message, double expected, double actual, int lre)
    {
        double testlre;
        if (expected == actual)
        {
            return;
        }
        if (expected == 0.0)
        {
            testlre = -1.0 * Math.log10(Math.abs(actual));
        }
        else
        {
            testlre = -1.0 * Math.log10(Math.abs(actual-expected)) + Math.log10(Math.abs(expected));
        }
        if ((int) Math.floor(testlre) < lre)
        {
            if (!message.isEmpty())
            {
                // use assertSame so that it fails and prints like the other assert errors
                assertSame(String.format("%s <LRE: %f>", message, testlre), expected, actual);
            }
            else
            {
                // use assertSame so that it fails and prints like the other assert errors
                assertSame(String.format("<LRE: %f>", testlre), expected, actual);
            }
        }
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error.
     * Log relative error measures the number of significant digits of agreement.
     * @param expected
     * @param actual
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(BigDecimal expected, BigDecimal actual, int lre)
    {
        assertEqualsLRE("", expected, actual, lre);
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error.
     * Log relative error measures the number of significant digits of agreement.
     * @param message message if the test fails
     * @param expected
     * @param actual
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(@NotNull String message, BigDecimal expected, BigDecimal actual, int lre)
    {
        if (expected == null && actual == null)
        {
            //return;
        }
        else if (expected == null || actual == null)
        {
            assertSame(message, expected, actual);
        }
        // if they are the same object, return
        else if (expected.equals(actual))
        {
            //return;
        }
        // if they are numerically equal, return
        else if (expected.compareTo(actual) == 0)
        {
            //return;
        }
        else
        {
            // turn the big decimal to a string and count the digits of agreement
            char[] s_expected = expected.toString().toCharArray();
            char[] s_actual = actual.toString().toCharArray();

            int minLength = Math.min(s_expected.length, s_actual.length);
            int testlre = 0;
            for (int i = 0; i < minLength; i++)
            {
                // if they are not equal, break
                if (s_expected[i] != s_actual[i])
                {
                    break;
                }
                // if they are equal, but one is E then break because the exponenet isn't compared
                else if (s_expected[i] == 'E' || s_expected[i] == 'e')
                {
                    break;
                }
                // if the deimal place is compared, skip it
                else if (s_expected[i] == '.')
                {
                    continue;
                }
                // otherwise, increase the count
                testlre++;
            }
            if (testlre < lre)
            {
                assertSame(message  + String.format(" LRE: <%d>", testlre), expected.toString(), actual.toString());
            }
        }
    }
}
