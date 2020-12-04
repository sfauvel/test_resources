package org.sfvl.demo_junit5;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DemoExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext context)  {
        System.out.println("DemoExtension.afterEach");
    }
}

