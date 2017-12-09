package dojo;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import jdk.nashorn.internal.ir.annotations.Ignore;

@RunWith(Cucumber.class)
@CucumberOptions(strict=true,
	glue = "glue/glueAuto",
	tags = "~@manuel",
	plugin = {"json:target/cucumber_auto.json"})
public class CucumberRunnerAutoTest {

}