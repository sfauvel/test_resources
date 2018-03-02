package org.sfvl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Example using a Rule to manage the driver. We need to init driver into the
 * rule and use this driver to init the one into the test.
 */
public class SeleniumLiveOnErrorWithRuleTest {

	SimpleWebApp webapp = new SimpleWebApp(8080);

	public ByteArrayOutputStream outStream = new ByteArrayOutputStream();

	public static  class BaseClassSelenium {

		public static ByteArrayOutputStream innerOutStream ;
		
		@Rule
		public TakeHandOnError console = new TakeHandOnError(new PrintStream(innerOutStream)) {
			public WebDriver initDriver() {
				return DriverFactory.getInstance().getChromeDriver("../driver/chrome-2.35");
			};
		};

		@Before 
		public void initDriver() {
			driver = console.getDriver();
			driver.get("http://localhost:8080/welcome");
		}
		protected WebDriver driver;
	}
	
	public static  class CheckSeleniumOnSuccess extends BaseClassSelenium{
		@Test
		public void testExecuteSelenium() throws Exception {
		}
	}
	
	public static  class CheckSeleniumOnFailure extends BaseClassSelenium{
		@Test
		public void testExecuteSelenium() throws Exception {
			driver.findElement(By.id("unknown")).click();
		}
	}

	@Before
	public void startWebApp() throws IOException {
		webapp.start();
		BaseClassSelenium.innerOutStream = outStream;
	}

	@After
	public void stopWebApp() {
		webapp.stop();
	}

	@Test
	public void should_not_fail() throws Exception {
		
		Result result = new JUnitCore().run(new Computer(), CheckSeleniumOnSuccess.class);

		assertEquals(0, result.getFailureCount());
		assertThat(getOutput()).isEmpty();
	}
	
	@Test
	public void should_find_element_and_retrieve_text_before_a_failing_test() throws Exception {

		injectInputText(new Command("driver.findElement(By.id(\"enter_button\")).getText()"));
		
		Result result = new JUnitCore().run(new Computer(), CheckSeleniumOnFailure.class);

		assertEquals(1, result.getFailureCount());
		assertThat(getOutput()).endsWith("Enter\n");
	}

	@Test
	public void should_not_find_element_before_a_failing_test() throws Exception {

		injectInputText(new Command("driver.findElement(By.id(\"unknown\")).getText()"));
		
		Result result = new JUnitCore().run(new Computer(), CheckSeleniumOnFailure.class);

		assertEquals(1, result.getFailureCount());
		assertThat(getOutput()).isEmpty();
	}
	
	@Test
	public void should_find_element_after_not_find_element_before_a_failing_test() throws Exception {

		injectInputText(new Command("driver.findElement(By.id(\"unknown\")).getText()\ndriver.findElement(By.id(\"enter_button\")).getText()"));
		
		Result result = new JUnitCore().run(new Computer(), CheckSeleniumOnFailure.class);

		assertEquals(1, result.getFailureCount());
		assertThat(getOutput()).endsWith("Enter\n");
	}
	
	private String getOutput() {
		return new String(outStream.toByteArray());
	}

	class Command {
		private final String command;
		
		public Command(String command) {
			this.command = command;
		}

		@Override
		public String toString() {
			return command + "\n";
		}
	}
	public final Command EXIT = new Command("!");
	private void injectInputText(Command command) throws UnsupportedEncodingException {
		InputStream in = new ByteArrayInputStream((command.toString() + EXIT).getBytes("UTF-8"));
		System.setIn(in);
	}

}
