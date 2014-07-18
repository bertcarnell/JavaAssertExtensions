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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Adds additional <code>Assert</code> methods to the JUnit implementation
 * 
 * @author Robert Carnell (bertcarnell@gmail.com)
 * @author Mariano Navas
 */
public class AssertExtensions
{
    /**
     * Prevent instantiation of this class.
     */
    private AssertExtensions() {}

    /**
     * Extension shorthand for <code>assertTrue(true)</code> to mirror <code>fail()</code>
     */
    public static void pass()
    {
        assertTrue(true);
    }
    
    /**
     * Cast a <code>Set</code> to an <code>ArrayList</code> of objects and test equality
     * @param expected A <code>Set</code> of expected objects
     * @param actual A <code>Set</code> of actual objects for comparison to expected
     */
    public static void assertSetsEqualsAsLists(Set<?> expected, Set<?> actual)
    {
        assertEquals(new ArrayList<>(expected), new ArrayList<>(actual));
    }
    
    /**
     * Assert that two <code>Lists</code> are equal, element by element
     * @param expected the expected <code>List</code>
     * @param actual the actual <code>List</code>
     */
    public static void assertListEquals(List<?> expected, List<?> actual)
    {
        if (expected == null && actual == null)
        {
            assertEquals("Both objects are null", expected, actual);
            return;
        }
        else if (expected == null || actual == null)
        {
            assertEquals("One object is null", expected, actual);
            return;
        }
        assertEquals("Lists have unequal sizes", expected.size(), actual.size());
        if (expected.size() > 0)
        {
            for (int i = 0; i < expected.size(); i++)
            {
                assertEquals("At least one element of the lists are different", expected.get(i), actual.get(i));
            }
        }
    }
    
    /**
     * Assert that two <code>Sets</code> are equal, element by element
     * @param expected the expected <code>Set</code>
     * @param actual the actual <code>Set</code>
     */
    public static void assertSetEquals(Set<?> expected, Set<?> actual)
    {
        if (expected == null && actual == null)
        {
            assertEquals("Both objects are null", expected, actual);
            return;
        }
        else if (expected == null || actual == null)
        {
            assertEquals("One object is null", expected, actual);
            return;
        }
        assertEquals("Sets have unequal sizes", expected.size(), actual.size());
        if (expected.size() > 0)
        {
            assertTrue("actual does not contain all of expected", actual.containsAll(expected));
        }
    }
    
    /**
     * Assert that two <code>Maps</code> are equal, element by element
     * @param expected the expected <code>Map</code>
     * @param actual the actual <code>Map</code>
     */
    public static void assertMapEquals(Map<?, ?> expected, Map<?, ?> actual)
    {
        if (expected == null && actual == null)
        {
            assertEquals("Both objects are null", expected, actual);
            return;
        }
        else if (expected == null || actual == null)
        {
            assertEquals("One object is null", expected, actual);
            return;
        }
        assertEquals("Sets have unequal sizes", expected.size(), actual.size());
        if (expected.size() > 0)
        {
            for (Entry<?,?> e : expected.entrySet())
            {
                assertTrue("maps do not contain the same keys", actual.containsKey(e.getKey()));
                assertEquals("maps do not have the same value for this key", e.getValue(), actual.get(e.getKey()));
            }
        }
    }
}
