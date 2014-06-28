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
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import static org.junit.Assert.assertSame;

/**
 * Extensions to the JUnit library for numeric argument related assertions
 * @author carnellr
 */
public class NumericAssertExtensions
{
    /**
     * Assert that expected and actual are equal to within a certain log relative error. Log relative error measures the number of
     * significant digits of agreement.
     *
     * @param expected expected value
     * @param actual actual value
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(double expected, double actual, int lre)
    {
        assertEqualsLRE("", expected, actual, lre);
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error. Log relative error measures the number of
     * significant digits of agreement.
     *
     * @param message message if the test fails
     * @param expected expected value
     * @param actual actual value
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
            testlre = -1.0 * Math.log10(Math.abs(actual - expected)) + Math.log10(Math.abs(expected));
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
     * Assert that expected and actual are equal to within a certain log relative error. Log relative error measures the number of
     * significant digits of agreement.
     *
     * @param expected expected value
     * @param actual actual value
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(BigDecimal expected, BigDecimal actual, int lre)
    {
        assertEqualsLRE("", expected, actual, lre);
    }

    /**
     * Assert that expected and actual are equal to within a certain log relative error. Log relative error measures the number of
     * significant digits of agreement.
     *
     * @param message message if the test fails
     * @param expected expected value
     * @param actual actual value
     * @param lre log relative error desired
     */
    public static void assertEqualsLRE(@NotNull String message, BigDecimal expected, BigDecimal actual, int lre)
    {
        if (expected == null && actual == null)
        {
            pass();
        }
        else if (expected == null || actual == null)
        {
            // use the same rules as assertSame
            assertSame(message, expected, actual);
        }
        // if they are the same object, return
        else if (expected.equals(actual))
        {
            pass();
        }
        // if they are numerically equal, return
        else if (expected.compareTo(actual) == 0)
        {
            pass();
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
                // if the decimal place is compared, skip it
                else if (s_expected[i] == '.')
                {
                    continue;
                }
                // otherwise, increase the count
                testlre++;
            }
            if (testlre < lre)
            {
                assertSame(message + String.format(" LRE: <%d>", testlre), expected.toString(), actual.toString());
            }
        }
    }
}
