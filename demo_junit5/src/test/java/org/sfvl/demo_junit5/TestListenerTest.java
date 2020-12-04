package org.sfvl.demo_junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DemoExtension.class)
public class TestListenerTest {

    @Test
    public void doSomething() {
        System.out.println("TestListenerTest.doSomething");
    }
}
