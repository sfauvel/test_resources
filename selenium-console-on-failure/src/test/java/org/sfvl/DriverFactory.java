package org.sfvl;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
	private static DriverFactory instance = new DriverFactory();

	public static DriverFactory getInstance() {
		return instance;
	}

	public WebDriver getDriver() {
		String browser = Optional.ofNullable(System.getenv("BROWSER")).orElse("").toUpperCase();
		System.out.println("BROWSER:" + browser);

		DriverType driverType;
		try {
			driverType = DriverType.valueOf(browser);
		} catch (IllegalArgumentException e) {
			driverType = getDefaultDriverType();
		}

		return driverType.driverDefinition.getDriver();
	}


	public DriverType getDefaultDriverType() {
		return DriverType.CHROMEHEADLESS;
	}
	
	enum DriverType {
		CHROME(new ChromeDriverDefinition()),
		CHROMEHEADLESS(new ChromeHeadlessDriverDefinition()),
		PHANTOMJS(new PhantomJSDriverDefinition()),
		FIREFOX(new FirefoxDriverDefinition()),
		FIREFOXHEADLESS(new FirefoxHeadlessDriverDefinition()),
		IE(null);

		private DriverDefinition driverDefinition;

		DriverType(DriverDefinition driverDefinition) {
			this.driverDefinition = driverDefinition;
		}
	}

	public static abstract class DriverDefinition {
		abstract WebDriver getDriver();

		protected Optional<String> getDriverPathFromEnv(String envVariable) {
			Optional<String> driverPath = Optional.ofNullable(System.getenv(envVariable));
			if (!driverPath.isPresent()) {
				// throw new RuntimeException("La variable " + envVariable + " n'est pas
				// définie");
				System.err.println("La variable " + envVariable + " n'est pas définie");
			}
			return driverPath;
		}
	}

	public static class ChromeDriverDefinition extends DriverDefinition {
		WebDriver getDriver() {
			System.setProperty("webdriver.chrome.driver", Paths.get(getDriverPath(), "chromedriver").toString());
			return new ChromeDriver(getOptions());
		}

		protected ChromeOptions getOptions() {
			return new ChromeOptions();
		}

		private String getDriverPath() {
			return getDriverPathFromEnv("SELENIUM_CHROME_DRIVER").orElse("../driver/chrome-2.35");
		}
	}

	public static class ChromeHeadlessDriverDefinition extends ChromeDriverDefinition {
		@Override
		protected ChromeOptions getOptions() {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("window-size=1200x600");
			return options;
		}
	}

	public static class PhantomJSDriverDefinition extends DriverDefinition {
		WebDriver getDriver() {
			System.setProperty("phantomjs.binary.path", Paths.get(getDriverPath(), "phantomjs").toString());
			return new PhantomJSDriver();
		}

		private String getDriverPath() {
			return getDriverPathFromEnv("SELENIUM_PHANTOMJS_DRIVER").orElse("../driver/phantomjs-2.1.1");
		}
	}

	public static class FirefoxDriverDefinition extends DriverDefinition {
		WebDriver getDriver() {
			System.setProperty("webdriver.gecko.driver", Paths.get(getDriverPath(), "geckodriver").toString());

			RemoteWebDriver driver = new FirefoxDriver();
		//	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			return driver;
		}

		private String getDriverPath() {
			return getDriverPathFromEnv("SELENIUM_FIREFOX_DRIVER").orElse("../driver/geckodriver-v0.20.0-linux64");
		}
	}


	public static class FirefoxHeadlessDriverDefinition extends DriverDefinition {
		WebDriver getDriver() {
			System.setProperty("webdriver.gecko.driver", Paths.get(getDriverPath(), "geckodriver").toString());

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("-headless");
			options.addArguments("window-size=1200x600");
			RemoteWebDriver driver = new FirefoxDriver(options);
		//	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			return driver;
		}

		private String getDriverPath() {
			return getDriverPathFromEnv("SELENIUM_FIREFOX_DRIVER").orElse("../driver/geckodriver-v0.20.0-linux64");
		}
	}
}
