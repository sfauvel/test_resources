package org.test;

import org.test.category.Slow;
import org.test.category.Special;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.fail;

/**
 * Test with a specific name
 */
public class ApplicationIT {
    @Test
    public void should_do_something() {
        System.out.println("ApplicationIT.should_do_something");
    }

    @Test
    @Category(Special.class)
    public void should_do_something_special() {
        System.out.println("ApplicationIT.should_do_something");
    }

    @Test
    @Category(Slow.class)
    public void should_do_something_long() {
        System.out.println("ApplicationIT.should_do_something_long");
    }


    @Test
    @Category({Special.class, Slow.class})
    public void should_do_something_long_and_special() {
        System.out.println("ApplicationIT.should_do_something_long");
    }
}
