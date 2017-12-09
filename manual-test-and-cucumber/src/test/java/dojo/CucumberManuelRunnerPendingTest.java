package dojo;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict=true,
	glue = "glue/glueManuelPending",
	tags = {"@manuel","~@ok","~@ko"},
	plugin = {"json:target/cucumber_pending.json"})
public class CucumberManuelRunnerPendingTest {

}
