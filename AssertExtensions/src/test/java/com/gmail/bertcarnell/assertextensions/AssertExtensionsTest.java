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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.fail;

/**
 * @author Rob Carnell (carnellr@battelle.org)
 */
public class AssertExtensionsTest {
    List<Double> expectedList;
    List<Double> actualList;
    Set<Double> expectedSet;
    Set<Double> actualSet;
    Map<String,Double> expectedMap;
    Map<String,Double> actualMap;
    
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

    public void testFailingAssert(ExceptionRunnable er) throws Exception, Throwable
    {
        try
        {
            er.run();
            throw new RuntimeException("Did not throw when should have throw an exception");
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

    /**
     * Test of assertListEquals method, of class AssertExtensions.
     * @throws java.lang.Throwable
     */
    @Test
    public void testAssertListEquals() throws Throwable {
        System.out.println("assertListEquals");
        expectedList = new ArrayList<>();
        expectedList.add(Double.valueOf(1.0));
        expectedList.add(Double.valueOf(2.0));
        actualList = new ArrayList<>();
        actualList.add(Double.valueOf(1.0));
        actualList.add(Double.valueOf(2.0));
        assertListEquals(expectedList, actualList);
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertListEquals(null, actualList);
            }
        });
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertListEquals(expectedList, null);
            }
        });
        assertListEquals(null, null);
        
        expectedList.add(Double.valueOf(3.0));
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertListEquals(expectedList, actualList);
            }
        });
        
        expectedList.remove(0);
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertListEquals(expectedList, actualList);
            }
        });
    }

    /**
     * Test of assertSetEquals method, of class AssertExtensions.
     */
    @Test
    public void testAssertSetEquals() throws Throwable {
        System.out.println("assertSetEquals");
        expectedSet = new HashSet<>();
        expectedSet.add(Double.valueOf(1.0));
        expectedSet.add(Double.valueOf(2.0));
        actualSet = new HashSet<>();
        actualSet.add(Double.valueOf(1.0));
        actualSet.add(Double.valueOf(2.0));
        assertSetEquals(expectedSet, actualSet);
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertSetEquals(null, actualSet);
            }
        });
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertSetEquals(expectedSet, null);
            }
        });
        assertSetEquals(null, null);
        
        expectedSet.add(Double.valueOf(3.0));
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertSetEquals(expectedSet, actualSet);
            }
        });
        
        expectedSet.remove(Double.valueOf(1.0));
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertSetEquals(expectedSet, actualSet);
            }
        });
    }

    /**
     * Test of assertMapEquals method, of class AssertExtensions.
     */
    @Test
    public void testAssertMapEquals() throws Throwable {
        System.out.println("assertMapEquals");
        expectedMap = new HashMap<>();
        expectedMap.put("A", Double.valueOf(1.0));
        expectedMap.put("B", Double.valueOf(2.0));
        actualMap = new HashMap<>();
        actualMap.put("A", Double.valueOf(1.0));
        actualMap.put("B", Double.valueOf(2.0));
        assertMapEquals(expectedMap, actualMap);
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertMapEquals(null, actualMap);
            }
        });
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertMapEquals(expectedMap, null);
            }
        });
        assertMapEquals(null, null);
        
        expectedMap.put("C", Double.valueOf(3.0));
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertMapEquals(expectedMap, actualMap);
            }
        });
        
        expectedMap.remove("A");
        testFailingAssert(new ExceptionRunnable() {
            @Override
            public void run() throws Throwable {
                assertMapEquals(expectedMap, actualMap);
            }
        });
    }
}
