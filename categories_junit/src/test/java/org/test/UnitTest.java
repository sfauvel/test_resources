package org.test;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.test.category.Critical;
import org.test.category.Slow;
import org.test.category.Special;


public class UnitTest {
    @Test
    public void should_do_something() {
        System.out.println("UnitTest.should_do_something");
    }

    @Test
    @Category(Critical.class)
    public void must_do_something() {
        System.out.println("UnitTest.must_do_something");
    }

    @Test
    @Category(Special.class)
    public void should_do_something_special() {
        System.out.println("UnitTest.should_do_something");
    }

    @Test
    @Category(Slow.class)
    public void should_do_something_long() {
        System.out.println("UnitTest.should_do_something_long");
    }


    @Test
    @Category({Special.class, Slow.class})
    public void should_do_something_long_and_special() {
        System.out.println("UnitTest.should_do_something_long");
    }
}
