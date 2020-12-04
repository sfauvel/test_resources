package org.sfvl.demo_junit5;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class RunTestFromCode {

    public static void main (String... args) {
        new RunTestFromCode().runAllTest();
    }
    public void runAllTest() {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectPackage("org.sfvl"))
                .filters(includeClassNamePatterns(".*FromCodeTest"))
                .build();

        // Need junit-platform-launcher
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
         launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        final TestExecutionSummary summary = listener.getSummary();
        System.out.println("=========================");
        System.out.println("Nb tests:" + summary.getTestsFoundCount());
        System.out.println("Succes:" + summary.getTestsSucceededCount());
        System.out.println("Echec:" + summary.getTestsFailedCount());
        System.out.println("=========================");

    }

}
