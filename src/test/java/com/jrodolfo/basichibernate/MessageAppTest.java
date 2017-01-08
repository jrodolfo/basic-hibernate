package com.jrodolfo.basichibernate;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-08
 */
public class MessageAppTest  extends TestCase {
    /**
     * create test case
     *
     * @param testName name of the test case
     */
    public MessageAppTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(MessageAppTest.class);
    }

    /**
     * rigorous test :)
     */
    public void testApp()
    {
        assertTrue(true);
    }
}
