/*
 * Copyright (c) 2014 Robert Carnell
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

/**
 * This class is an interface similar to Runnable that allows for the <code>run()</code>
 * method to throw a <code>Throwable</code>.  If <code>Runnable</code> is used to call a a method that throws
 * then it would normally need to be surrounded with a <code>try-catch</code> which defeats the purpose
 * of this testing library.
 * 
 * @author Rob Carnell
 */
public interface ExceptionRunnable {
    /**
     * A method that can execute any series of commands and may throw a <code>Throwable</code>
     * @throws Throwable 
     */
    public void run() throws Throwable;
}
