/*
 * Copyright 2013 Robert Carnell
 */
package org.bertcarnell.test.assertextensions;

import java.beans.Statement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 */
public class AssertExtensions 
{
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
    public static void assertThrows(Class<? extends Throwable> expectedException, Object target, String methodName, Object... arguments) 
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
    public static void assertThrows(String message, Class<? extends Throwable> expectedException, Object target, String methodName, Object... arguments) 
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
    public static void assertConstuctorThrows(Class<? extends Throwable> expectedException, Constructor<?> constr, Object... arguments)
    {
        try
        {
            if (constr != null) {
                constr.newInstance(arguments);
            }
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
}
