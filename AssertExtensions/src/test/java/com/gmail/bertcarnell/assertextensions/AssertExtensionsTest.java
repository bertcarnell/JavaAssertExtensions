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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.gmail.bertcarnell.assertextensions.AssertExtensions.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rob Carnell (carnellr@battelle.org)
 */
public class AssertExtensionsTest {
    
    public AssertExtensionsTest() {
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
     * Test of pass method, of class AssertExtensions.
     */
    @Test
    public void testPass() {
        System.out.println("pass");
        pass();
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
}
