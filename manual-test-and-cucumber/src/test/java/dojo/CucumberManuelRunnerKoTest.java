package dojo;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.After;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
	glue = "glue/glueManuelKo",
	tags = { "@manuel", "@ko" },
	plugin = {"json:target/cucumber_ko.json"})
public class CucumberManuelRunnerKoTest {


}
