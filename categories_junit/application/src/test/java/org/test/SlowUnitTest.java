package org.test;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.test.category.Slow;


@Category(Slow.class)
public class SlowUnitTest {
    @Test
    public void should_do_something_long() {
        System.out.println("SlowUnitTest.should_do_something_long");
    }

    @Test
    public void should_do_something_very_long() {
        System.out.println("SlowUnitTest.should_do_something_very_long");
    }

}
