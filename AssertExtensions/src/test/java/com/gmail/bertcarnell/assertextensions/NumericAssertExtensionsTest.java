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
import static com.gmail.bertcarnell.assertextensions.NumericAssertExtensions.*;
import java.math.BigDecimal;
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
public class NumericAssertExtensionsTest {
    
    public NumericAssertExtensionsTest() {
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