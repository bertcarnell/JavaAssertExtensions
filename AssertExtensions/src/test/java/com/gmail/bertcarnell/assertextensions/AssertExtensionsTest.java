/*
 * Copyright 2013 Robert Carnell
 */
package com.gmail.bertcarnell.assertextensions;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.gmail.bertcarnell.assertextensions.AssertExtensions.*;

/**
 * @author Rob Carnell (carnellr@battelle.org)
 */
public class AssertExtensionsTest {
    
    public AssertExtensionsTest() {
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
     * Test of assertThrowsAlternate method, of class AssertExtensions.
     * @throws Exception 
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testAssertThrowsAlternate_4args_1() throws Exception {
        System.out.println("assertThrowsAlternate using varArgs");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                assertThrowsAlternate(NumberFormatException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                assertThrowsAlternate(ArithmeticException.class, new Double(0), "parseDouble", "a");
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                assertThrowsAlternate(NumberFormatException.class, new Double(0), "parseDouble", "1");
            }
        }.test();
    }

    /**
     * Test of assertThrowsAlternate method, of class AssertExtensions.
     * @throws Exception 
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testAssertThrowsAlternate_4args_2() throws Exception {
        System.out.println("assertThrowsAlternate using Object array");
        new AssertExtenstionsTestTemplate(){
            @Override
            void assertThatIsExpectedToPass() throws Exception {
                Class[] parameterTypes = new Class<?>[1];
                parameterTypes[0] = String.class;
                Method method = Double.class.getMethod("parseDouble", parameterTypes);
                Object[] parameters = new Object[1];
                parameters[0] = "a";
                assertThrowsAlternate(NumberFormatException.class, new Double(0), method, parameters);
            }
            @Override
            void assertThatIsExpectedToFailOnWrongException() throws Exception {
                Class[] parameterTypes = new Class<?>[1];
                parameterTypes[0] = String.class;
                Method method = Double.class.getMethod("parseDouble", parameterTypes);
                Object[] parameters = new Object[1];
                parameters[0] = "a";
                assertThrowsAlternate(ArithmeticException.class, new Double(0), method, parameters);
            }
            @Override
            void assertThatIsExpectedToFailOnMissingException() throws Exception {
                Class[] parameterTypes = new Class<?>[1];
                parameterTypes[0] = String.class;
                Method method = Double.class.getMethod("parseDouble", parameterTypes);
                Object[] parameters = new Object[1];
                parameters[0] = "1";
                assertThrowsAlternate(NumberFormatException.class, new Double(0), method, parameters);
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

    /**
     * Test of pass method, of class AssertExtensions.
     */
    @Test
    public void testPass() {
        System.out.println("pass");
        pass();
    }

    private void assertEqualsLREWillFail(double expected, double actual, int lre)
    {
        try
        {
            assertEqualsLRE(expected, actual, lre);
            throw new NoSuchMethodException("Not thrown");
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
    private void assertEqualsLREWillFail(String message, double expected, double actual, int lre)
    {
        assertEqualsLREWillFail(expected, actual, lre);
    }

    private void assertEqualsLREWillFail(BigDecimal expected, BigDecimal actual, int lre)
    {
        try
        {
            assertEqualsLRE(expected, actual, lre);
            throw new NoSuchMethodException("Not thrown");
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
    private void assertEqualsLREWillFail(String message, BigDecimal expected, BigDecimal actual, int lre)
    {
        assertEqualsLREWillFail(expected, actual, lre);
    }
    
    /**
     * Test of assertEqualsLRE method, of class AssertExtensions.
     */
    @Test
    public void testAssertEqualsLRE_3args_1() {
        System.out.println("assertEqualsLRE");
        double expected = 1234.5678;
        double actual = 1234.5679;
        // will pass
        assertEqualsLRE(expected, actual, 7);
        assertEqualsLRE(expected, actual, 6);
        assertEqualsLRE(expected, actual, 0);
        assertEqualsLRE(expected, actual, -1);
        // will fail
        assertEqualsLREWillFail(expected, actual, 8);
        assertEqualsLREWillFail(expected, actual, 9);
        // will pass
        assertEqualsLRE(0.0, 1E-12, 12);
        assertEqualsLRE(0.0, 1.2345E-3, 2);
        // will fail
        assertEqualsLREWillFail(0.0, 1.2345E-3, 3);
        assertEqualsLREWillFail(0.0, 1.2345E-3, 4);
        assertEqualsLREWillFail(0.0, 100.0, 1);
    }

    /**
     * Test of assertEqualsLRE method, of class AssertExtensions.
     */
    @Test
    public void testAssertEqualsLRE_4args_1() {
        System.out.println("assertEqualsLRE");
        String message = "abc";
        double expected = 1.23456E-50;
        double actual = 1.23499E-50;
        // will pass
        assertEqualsLRE(message, expected, actual, 3);
        // will fail
        assertEqualsLREWillFail(message, expected, actual, 4);
        expected = 0.00067890234;
        actual = 0.00067891111;
        // will pass
        assertEqualsLRE(message, expected, actual, 4);
        // will fail
        assertEqualsLREWillFail(message, expected, actual, 5);
    }

    /**
     * Test of assertEqualsLRE method, of class AssertExtensions.
     */
    @Test
    public void testAssertEqualsLRE_3args_2() {
        System.out.println("assertEqualsLRE");
        BigDecimal expected = new BigDecimal("123456712345671234567");
        BigDecimal actual = new BigDecimal("123456712345671234568");
        // will pass
        assertEqualsLRE(expected, actual, 20);
        // will fail
        assertEqualsLREWillFail(expected, actual, 21);
        // will pass
        assertEqualsLRE(new BigDecimal("0.0"), new BigDecimal("0.0000"), 4);
        assertEqualsLRE(new BigDecimal("111.222"), new BigDecimal("111.222"), 100);
        assertEqualsLREWillFail(new BigDecimal("100.0"), new BigDecimal("0.003"), 2);
    }

    /**
     * Test of assertEqualsLRE method, of class AssertExtensions.
     */
    @Test
    public void testAssertEqualsLRE_4args_2() {
        System.out.println("assertEqualsLRE");
        BigDecimal expected = new BigDecimal("123456712345671234567");
        BigDecimal actual = new BigDecimal("123456712345671234568");
        String message = "abc";
        // will pass
        assertEqualsLRE(message, expected, actual, 20);
        // will fail
        assertEqualsLREWillFail(message, expected, actual, 21);
    }
}
