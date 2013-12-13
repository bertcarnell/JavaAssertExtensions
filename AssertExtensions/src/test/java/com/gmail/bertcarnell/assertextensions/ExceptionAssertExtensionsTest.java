/*
 * Copyright (c) 2013 Robert Carnell
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
import static com.gmail.bertcarnell.assertextensions.ExceptionAssertExtensions.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author carnellr
 */
public class ExceptionAssertExtensionsTest {
    
    public ExceptionAssertExtensionsTest() {
    }

    /**
     * create a class that uses the "template" pattern to avoid writing re-useable code
     * <code>public abstract class ClassName {</code>
     * <code>   pubic void method() {</code>
     * <code>       // do reusable stuff</code>
     * <code>       stuffThatChanges();</code>
     * <code>       // do more reusable stuff</code>
     * <code>   }</code>
     * <code>   abstract void stuffThatChanges();</code>
     * <code>}</code>
     * Called with
     * <code>new ClassName {</code>
     * <code>   void stuffThatChanges() {</code>
     * <code>       // do stuff</code>
     * <code>   }</code>
     * <code>}.method()</code>
     */
    public abstract class AssertExtenstionsTestTemplate extends Object
    {
        public void test() throws Exception
        {
            assertThatIsExpectedToPass();
            try
            {
                assertThatIsExpectedToFailOnWrongException();
                throw new NoSuchMethodException("Did not throw");
            }
            catch (AssertionError e)
            {
                System.out.println("\tExpected Exception: " + e.getMessage());
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown:" + e2.getMessage());
            }
            try
            {
                assertThatIsExpectedToFailOnMissingException();
                throw new NoSuchMethodException("Did not throw");
            }
            catch (AssertionError e)
            {
                System.out.println("\tExpected Exception: " + e.getMessage());
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown:" + e2.getMessage());
            }
        }
        abstract void assertThatIsExpectedToPass() throws Exception;
        abstract void assertThatIsExpectedToFailOnWrongException() throws Exception;
        abstract void assertThatIsExpectedToFailOnMissingException() throws Exception;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of assertThrows method, of class ExceptionAssertExtensions.
     */
    @Test
    @Ignore
    public void testAssertThrows_Class_Runnable() {
        System.out.println("assertThrows");
        fail("The test case is a prototype.");
    }

    /**
     * Test of assertThrows method, of class ExceptionAssertExtensions.
     */
    @Test
    @Ignore
    public void testAssertThrows_3args() {
        System.out.println("assertThrows");
        fail("The test case is a prototype.");
    }

    /**
     * Test of asserThrowsAndDoAssertsInCatch method, of class ExceptionAssertExtensions.
     */
    @Test
    @Ignore
    public void testAsserThrowsAndDoAssertsInCatch() {
        System.out.println("asserThrowsAndDoAssertsInCatch");
        fail("The test case is a prototype.");
    }

    /**
     * Test of assertSetsEqualsAsLists method, of class ExceptionAssertExtensions.
     */
    @Test
    public void testAssertSetsEqualsAsLists() {
        System.out.println("assertSetsEqualsAsLists");
        int setSize = 5;
        Set<Double> expected = new HashSet<>(setSize);
        Set<Double> actual = new HashSet<>(setSize);
        for (int i = 0; i < setSize; i++)
        {
            expected.add(Double.valueOf((double) i));
            actual.add(Double.valueOf((double) i));
        }
        assertSetsEqualsAsLists(expected, actual);
    }

    /**
     * Test of assertThrowsAndDoAssertsInCatch method, of class ExceptionAssertExtensions.
     */
    @Test
    @Ignore
    public void testAssertThrowsAndDoAssertsInCatch() {
        System.out.println("assertThrowsAndDoAssertsInCatch");
        fail("The test case is a prototype.");
    }

    /**
     * Test of assertThrows method, of class AssertExtensions.
     * @throws Exception 
     */
    @Test
    public void testAssertThrows_4args() throws Exception {
        System.out.println("assertThrows");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows(ArithmeticException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "1");
            }
        }.test();
    }

    /**
     * Test of assertThrows method, of class AssertExtensions.
     * @throws Exception 
     */
    @Test
    public void testAssertThrows_5args() throws Exception{
        System.out.println("assertThrows with message check");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows("For input string: \"a\"", ArithmeticException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class, new Double(0), "parseDouble", "1");
            }
        }.test();

        // will fail because exception message is wrong
        try
        {
            assertThrows("a message", NumberFormatException.class, new Double(0), "parseDouble", "a");
            throw new NoSuchMethodException("Did not throw");
        }
        catch (AssertionError e)
        {
            System.out.println("\tExpected Exception: " + e.getMessage());
            pass();
        }
        catch (Exception e2)
        {
            fail("Wrong exception thrown:" + e2.getMessage());
        }
    }

    /**
     * Test of assertConstuctorThrows method, of class AssertExtensions.
     * @throws NoSuchMethodException
     * @throws Exception  
     */
    @Test
    public void testAssertConstructorThrows() throws NoSuchMethodException, Exception {
        System.out.println("assertConstuctorThrows");
        
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertConstuctorThrows(NumberFormatException.class, Double.class.getConstructor(String.class), "a");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertConstuctorThrows(ArithmeticException.class, Double.class.getConstructor(String.class), "a");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertConstuctorThrows(NumberFormatException.class, Double.class.getConstructor(double.class), 1.0);
            }
        }.test();

        // will fail because exception is not thrown
        try
        {
            assertConstuctorThrows(NumberFormatException.class, Double.class.getConstructor(String.class), "1.0");
            throw new NoSuchMethodException("Did not throw");
        }
        catch (AssertionError e)
        {
            System.out.println("\tExpected Exception: " + e.getMessage());
            pass();
        }
        catch (Exception e2)
        {
            fail("Wrong exception thrown:" + e2.getMessage());
        }
    }
}
