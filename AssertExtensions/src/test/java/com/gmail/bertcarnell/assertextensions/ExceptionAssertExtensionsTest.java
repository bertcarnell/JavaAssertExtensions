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
import java.lang.reflect.Constructor;
import javax.validation.constraints.NotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carnellr
 */
public class ExceptionAssertExtensionsTest {
    
    public ExceptionAssertExtensionsTest() {
    }

    /**
     * create a class that uses the "template" pattern to avoid rewriting re-useable code
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
        /**
         * Execute a test that calls all abstract methods in this class
         * @throws Exception 
         */
        public void test() throws Exception
        {
            assertThatIsExpectedToPass();
            try
            {
                assertThatIsExpectedToFailOnWrongException();
                throw new RuntimeException("Did not throw when should have failed on wrong exception");
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
        /**
         * Encloses a test in the form of an assertion that is expected to pass
         * @throws Exception 
         */
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
     * Test assertThrows
     * <ul>
     * <li>Exception</li>
     * <li>Runnable that throws</li>
     * </ul>
     */
    @Test
    public void testAssertThrows_Exception_Runnable() throws Exception {
        System.out.println("testAssertThrows_Exception_Runnable");
        assertThrows(NumberFormatException.class, new Runnable(){
            @Override
            public void run() {
                throw new NumberFormatException("Test Exception");
            }
        });
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
     * Test assertThrows
     * <ul>
     * <li>Exception</li>
     * <li>Runnable that throws</li>
     * <li>Custom Message</li>
     * </ul>
     */
    @Test
    public void testAssertThrows_Exception_Runnable_Message() throws Exception {
        System.out.println("testAssertThrows_Exception_Runnable_Message");
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
     * Test of asserThrowsAndDoAssertsInCatch
     * <ul>
     * <li>Exception</li>
     * <li>ExceptionAssertionsPerformer</li>
     * </ul>
     */
    @Test
    public void testAssertThrowsAndDoAssertsInCatch_Exception_Performer() throws Exception {
        System.out.println("testAssertThrowsAndDoAssertsInCatch_Exception_Performer");
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
     * Test of asserThrowsAndDoAssertsInCatch
     * <ul>
     * <li>Exception</li>
     * <li>ExceptionAssertionsPerformer</li>
     * <li>Custom Message</li>
     * </ul>
     */
    @Test
    public void testAssertThrowsAndDoAssertsInCatch_Exception_Performer_Message() throws Exception {
        System.out.println("testAssertThrowsAndDoAssertsInCatch_Exception_Performer_Message");
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
     * Test assertThrows
     * <ul>
     * <li>Exception</li>
     * <li>Class</li>
     * <li>method</li>
     * <li>args</li>
     * </ul>
     * @throws Exception 
     */
    @Test
    public void testAssertThrows_Exception_Class_Method_Args() throws Exception {
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
                assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "1.0");
            }
        }.test();
    }

    /**
     * Test assertThrows
     * <ul>
     * <li>Exception Message</li>
     * <li>Exception</li>
     * <li>Class</li>
     * <li>method</li>
     * <li>args</li>
     * </ul>
     * @throws Exception 
     */
    @Test
    public void testAssertThrows_ExMessage_Exception_Class_Method_Args() throws Exception{
        System.out.println("testAssertThrows_ExMessage_Exception_Class_Method_Args");
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
     * Test of assertConstuctorThrows
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
    
    /**
     * Test assertThrows
     * <ul>
     * <li>Exception Message</li>
     * <li>Exception</li>
     * <li>Runnable</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testAssertThrows_ExMessage_Exception_Runnable() throws Exception
    {
        System.out.println("testAssertThrows_ExMessage_Exception_Runnable");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows("For input string: \"a\"", ArithmeticException.class, new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("1.0");
                    }
                });
            }
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrows("a message", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                });
            }
        }.test();
    }

    /**
     * Test assertThrows
     * <ul>
     * <li>Exception Message</li>
     * <li>Exception</li>
     * <li>Runnable</li>
     * <li>Custom Message</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testAssertThrows_ExMessage_Exception_Runnable_Message() throws Exception
    {
        System.out.println("testAssertThrows_ExMessage_Exception_Runnable_Message");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrows("For input string: \"a\"", ArithmeticException.class, new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrows("For input string: \"a\"", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("1.0");
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrows("a message", NumberFormatException.class,  new Runnable() {
                    @Override
                    public void run() {
                        Double.parseDouble("a");
                    }
                }, "custom message");
            }
        }.test();
    }
    
    /*    public static <T extends Throwable> void assertThrowsSpecificException(String excMessage,
            @NotNull Class<T> excType, @NotNull ExceptionAssertionsPerformer<T> excAssertsPerformer,
            String customFailMessage)*/
    /**
     * Test assertThrowsSpecificException
     * <ul>
     * <li>Exception Message</li>
     * <li>Exception</li>
     * <li>ExceptionAssertionsPerformer</li>
     * <li>Custom Message</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testAssertThrowsSpecificException() throws Exception
    {
        System.out.println("testAssertThrowsSpecificException");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrowsSpecificException("For input string: \"a\"", 
                        NumberFormatException.class,  new ExceptionAssertionsPerformer() {
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }

                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                        // NA
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrowsSpecificException("For input string: \"a\"", ArithmeticException.class, 
                        new ExceptionAssertionsPerformer() {
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }

                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrowsSpecificException("For input string: \"a\"", NumberFormatException.class,  
                        new ExceptionAssertionsPerformer() {
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("1.0");
                    }

                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                    }
                }, "custom message");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongExceptionMessage() throws Exception {
                assertThrowsSpecificException("a message", NumberFormatException.class,  
                        new ExceptionAssertionsPerformer() {
                    @Override
                    public void performThrowingAction() {
                        Double.parseDouble("a");
                    }

                    @Override
                    public void performAssertionsAfterCatch(Object th) throws Exception {
                    }
                }, "custom message");
            }
        }.test();
    }
}
