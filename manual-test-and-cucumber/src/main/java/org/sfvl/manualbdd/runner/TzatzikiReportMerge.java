package org.sfvl.manualbdd.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.DocumentException;

import gutenberg.itext.FontModifier;
import gutenberg.itext.Styles;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;

public class TzatzikiReportMerge {

	
	private static final String REPORTS_TZATZIKI = "reports/tzatziki";
	private static final String BUILD_DIR = ".";
	private String featurePath;

	public TzatzikiReportMerge(String featurePath) {
		this.featurePath = featurePath;
	}
	
	public void mergeReports() throws IOException, DocumentException {
		mergeReports(reportsList());
	}

	public void mergeReports(List<String> reports) throws IOException, DocumentException {
	        
        Map<String, FeatureExec> mapFeature = new HashMap<String, FeatureExec>();

        for (String report : reports) {
        	buildResult(mapFeature, report);
		}
          		       
        Collection<FeatureExec> execs = mapFeature.values();
        	 
        File fileOut = new File(buildDir(), "reports/report_merge_tzatziki.pdf");
        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(true)
                                .displayScenarioTags(true)
                                .declareProperty("imageDir",
                                        new File(baseDir(), "/features/images").toURI().toString())
                                .adjustFont(Styles.TABLE_HEADER_FONT, new FontModifier().size(10.0f))
                )
                .title("Application specification ")
                .subTitle("Technical & Functional specifications")
               // .markup(Markdown.fromUTF8Resource("/myapp/feature/preamble.md"))
                .features(execs)
                .sampleSteps()
                .generate(fileOut);
    }

	private List<String> reportsList() {
		List<String> reports = new ArrayList<String>();
		for (Type type : Arrays.asList(Type.Ok, Type.Ko, Type.Pending)) {
			reports.add(REPORTS_TZATZIKI + "/" + type.name().toLowerCase() +"/exec.json");
		}
		return reports;
	}
	
	private void buildResult(Map<String, FeatureExec> mapFeature, String jsonPath) throws IOException {
		try {
			List<FeatureExec> execs = loadExec(new File(buildDir(), jsonPath));
			mergeScenarios(mapFeature, execs);
		} catch (FileNotFoundException e) {
			System.out.println("No file: " +jsonPath);
		}
	}

    private static List<FeatureExec> loadExec(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return new JsonIO().load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
	private void mergeScenarios(Map<String, FeatureExec> mapFeature, List<FeatureExec> execsOk2) {
		for (FeatureExec featureExec : execsOk2) {
			
			String featureKey = featureKey(featureExec);
			if (!mapFeature.containsKey(featureKey)) {
				mapFeature.put(featureKey, featureExec);	
			} else {
				for (ScenarioExec scenarioExec : featureExec.scenario()) {						
					mapFeature.get(featureKey).declareScenario(scenarioExec);
				}
			}
						
		}
	}

	private String featureKey(FeatureExec featureExec) {
		return featureExec.name() + ":" + featureExec.uri();
	}

    private static File buildDir() {
        return new File(BUILD_DIR);
    }

    private static File baseDir() {
        return new File(BUILD_DIR);
    }

	public void runTestsAndMergeReports() {
		ReportGenerator manualTestReport = new ReportGenerator(featurePath) {
			@Override
			public List<String> pluginOptions(Type type) {
				return Arrays.asList("tzatziki.analysis.exec.gson.JsonEmitterReport:" + REPORTS_TZATZIKI + "/"+type.name().toLowerCase());
			}
		};
		manualTestReport.runTests();
		
		try {
			mergeReports();
		} catch (IOException e ) {
			System.err.println("Error while generating Tzatziki report");
			System.err.println(e.getMessage());
		} catch (DocumentException e) {
			System.err.println("Error while generating Tzatziki report");
			System.err.println(e.getMessage());
		}
	}

	
	public static void main(String[] args) throws IOException, DocumentException {
		String featurePath;
		if (args.length == 1) {
			System.out.println("Répertoire des features: " + args[0]);
			featurePath = args[0];
		} else {
			featurePath = "./features";
		}
		new TzatzikiReportMerge(featurePath).runTestsAndMergeReports();
	}
	
}
