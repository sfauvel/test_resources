package org.test.integration;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test in a specific package with a standard test name.
 */
public class MyTest {
    @Test
    public void should_do_something() {
        System.out.println("integration.MyTest.should_do_something");
    }

}
