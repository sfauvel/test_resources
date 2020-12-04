package org.sfvl.demo_junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

public class TimeoutTest {

    private long counter = 0;
    private static final int COUNTER_MAX = 100000;

//    // This test failed because of timeout.
//    // If there is no Thread.sleep in function called, it waits end of the code before failing.
//    // If there is a break, it fails just after timeout
//    @Test
//    @Timeout(value = 1, unit = TimeUnit.MILLISECONDS)
//    public void should_fail_because_of_annotation_timeout() throws InterruptedException {
//        myCode();
//    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void should_pass_because_no_timeout_using_timeout_annotation() throws InterruptedException {
        myCode();
    }

    @Test
    public void should_assertTimeout_at_the_end_when_no_sleep_in_code() throws InterruptedException {

        try {
            assertTimeout(ofMillis(10), () -> myCode());
        } catch (AssertionError e) {
            // assertTimeout failed because it not finished before timeout but execution is not stopped before the end.
            assertEquals(COUNTER_MAX, counter);
            return;
        }
        Assertions.fail("assertTimeout should throw an exception");
    }

    @Test
    public void test_finish_before_timeout() throws InterruptedException {
        int result = assertTimeoutPreemptively(ofSeconds(10), () -> myCode());
        assertEquals(1, result);
        assertEquals(COUNTER_MAX, counter);
    }

    @Test
    public void test_finish_after_timeout() throws InterruptedException {
        try {
            assertTimeoutPreemptively(ofMillis(10), () -> myCode());
        } catch (AssertionError e) {
            // assertTimeout failed because it not finished before timeout and execution is stopped when timeout is reached.
            assertTrue(counter < COUNTER_MAX);
            return;
        }
        Assertions.fail("Timeout should throw an exception");
    }

    private int myCode() throws InterruptedException {
        long i=0;
        while (i < COUNTER_MAX) {
            i++;
            counter++;
            System.out.println("Counter:" + i);
        }
        return 1;
    }


}
