package org.sfvl.manualbdd.runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportGenerator {

	private static final String BUILD_DIR = ".";
	private final String featurePath;

	public ReportGenerator(String featurePath) {
		this.featurePath = featurePath;
	}

	public void runTests() {
		runTest(Type.Ok, featurePath);
		runTest(Type.Ko, featurePath);
		runTest(Type.Pending, featurePath);
	}

	private void runTest(Type type, String features) {
		List<String> arguments = new ArrayList<String>();
		arguments.addAll(Arrays.asList("--glue", "org.sfvl.manualbdd.steps." + type.name().toLowerCase()));
		for (String pluginOption : pluginOptions(type)) {
			arguments.addAll(Arrays.asList("--plugin", pluginOption));
		}
		arguments.addAll(Arrays.asList("--tags", "@manual,@manuel"));
		for (String tag : type.tags) {
			arguments.addAll(Arrays.asList("--tags", tag));
		}
		arguments.add(features);
		try {
			cucumber.api.cli.Main.run(arguments.toArray(new String[0]), Thread.currentThread().getContextClassLoader());
		} catch (Throwable e) {
			System.err.println("Not able to execute " + type + " with " + features);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<String> pluginOptions(Type type) {
		return new ArrayList<String>(Arrays.asList("json:reports/cucumber/" + type.name().toLowerCase() + ".json"));
	}


	public static void main(String[] args) throws IOException {
		String featurePath;
		if (args.length == 1) {
			featurePath = args[0];
		} else {
			featurePath = "./features";
		}

		System.out.println("Features path: " + featurePath);
		
		ReportGenerator manualTestReport = new ReportGenerator(featurePath);
		manualTestReport.runTests();

	}
}
