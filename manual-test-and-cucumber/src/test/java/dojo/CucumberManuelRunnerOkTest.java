package dojo;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict=true,
	glue = "glue/glueManuelOk",
	tags = {"@manuel","@ok"},
	plugin = {"json:target/cucumber_ok.json"})
public class CucumberManuelRunnerOkTest {

}
