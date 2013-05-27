/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bertcarnell.test.assertextensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import static org.bertcarnell.test.assertextensions.AssertExtensions.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
                fail("Did not throw");
            }
            catch (AssertionError e)
            {
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown");
            }
            try
            {
                assertThatIsExpectedToFailOnMissingException();
                fail("Did not throw");
            }
            catch (AssertionError e)
            {
                pass();
            }
            catch (Exception e2)
            {
                fail("Wrong exception thrown");
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
            fail("Did not throw");
        }
        catch (AssertionError e)
        {
            pass();
        }
        catch (Exception e2)
        {
            fail("Wrong exception thrown");
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
            fail("Did not throw");
        }
        catch (AssertionError e)
        {
            pass();
        }
        catch (Exception e2)
        {
            fail("Wrong exception thrown");
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
}
