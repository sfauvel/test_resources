package org.sfvl.demo_junit5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunFromCodeTest {

    @Test
    public void should_pass() {
        assertEquals(2, 1+1);
    }
}
