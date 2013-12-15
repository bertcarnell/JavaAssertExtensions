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
                throw new NoSuchMethodException("Did not throw when should have failed on wrong exception");
            }
            catch (AssertionError e)
            {
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown:" + e2.getMessage());
            }
            try
            {
                assertThatIsExpectedToFailOnMissingException();
                throw new NoSuchMethodException("Did not throw when should have failed on missing exception");
            }
            catch (AssertionError e)
            {
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown:" + e2.getMessage());
            }
            try
            {
                assertThatIsExpectedToFailOnWrongExceptionMessage();
                throw new NoSuchMethodException("Did not throw when should have failed on wrong exception message");
            }
            catch (AssertionError e)
            {
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
        void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {fail("prototype");};
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
    public void testAssertThrows_Class_Runnable() throws Exception {
        System.out.println("assertThrows");
        assertThrows(NumberFormatException.class, new Runnable(){
            @Override
            public void run() {
                throw new NumberFormatException("Test Exception");
            }
        });
        System.out.println("assertThrows");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows(NumberFormatException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows(ArithmeticException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows(NumberFormatException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("1.0");
                    }
                });
            }
        }.test();
    }

    /**
     * Test of assertThrows method, of class ExceptionAssertExtensions.
     */
    @Test
    public void testAssertThrows_3args() throws Exception {
        System.out.println("assertThrows");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows(NumberFormatException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                }, "Custom Message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows(ArithmeticException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                }, "Custom Message");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows(NumberFormatException.class, new Runnable(){
                    @Override
                    public void run() {
                        Double.parseDouble("1.0");
                    }
                }, "Custom Message");
            }
        }.test();
    }

    /**
     * Test of asserThrowsAndDoAssertsInCatch method, of class ExceptionAssertExtensions.
     */
    @Test
    public void testAssertThrowsAndDoAssertsInCatch_2args() throws Exception {
        System.out.println("assertThrowsAndDoAssertsInCatch 2 arguments");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrowsAndDoAssertsInCatch(NumberFormatException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        // if this metod is called, we know that the object is a NumberFormatException or assignable from NumberFormatException
                        NumberFormatException nfe = (NumberFormatException) th;
                        assertEquals(nfe.getMessage(), "For input string: \"a\"");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrowsAndDoAssertsInCatch(ArithmeticException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        fail("should not get here");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrowsAndDoAssertsInCatch(ArithmeticException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("1.0");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        fail("should not get here");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrowsAndDoAssertsInCatch(NumberFormatException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        // if this metod is called, we know that the object is a NumberFormatException or assignable from NumberFormatException
                        NumberFormatException nfe = (NumberFormatException) th;
                        assertEquals(nfe.getMessage(), "wrong message");
                    }
                });
            }
        }.test();
    }

    /**
     * Test of assertThrowsAndDoAssertsInCatch method, of class ExceptionAssertExtensions.
     */
    @Test
    public void testAssertThrowsAndDoAssertsInCatch_3args() throws Exception {
        System.out.println("assertThrowsAndDoAssertsInCatch 3 arguments");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrowsAndDoAssertsInCatch(NumberFormatException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        // if this metod is called, we know that the object is a NumberFormatException or assignable from NumberFormatException
                        NumberFormatException nfe = (NumberFormatException) th;
                        assertEquals(nfe.getMessage(), "For input string: \"a\"");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrowsAndDoAssertsInCatch(ArithmeticException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        fail("should not get here");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrowsAndDoAssertsInCatch(ArithmeticException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("1.0");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        fail("should not get here");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrowsAndDoAssertsInCatch(NumberFormatException.class, new ExceptionAssertionsPerformer(){
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }
                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        // if this metod is called, we know that the object is a NumberFormatException or assignable from NumberFormatException
                        NumberFormatException nfe = (NumberFormatException) th;
                        assertEquals(nfe.getMessage(), "wrong message");
                    }
                }, "custom message");
            }
        }.test();
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
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrows("a message", NumberFormatException.class, new Double(0), "parseDouble", "a");
            }
        }.test();
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

        // will fail because exception is not thrown with a different class than above
        // Double.class.getConstructor(double.class)
        // versus
        // Double.class.getConstructor(String.class)
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
