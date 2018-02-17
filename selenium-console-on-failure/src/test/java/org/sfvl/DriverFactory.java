package org.sfvl;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
    private static final String CHROME_DRIVER_PATH = "SELENIUM_CHROME_DRIVER";
    private static final String PHANTOMJS_DRIVER_PATH = "SELENIUM_PHANTOMJS_DRIVER";
    private static final String FIREFOX_DRIVER_PATH = "SELENIUM_FIREFOX_DRIVER";
    private static DriverFactory instance = new DriverFactory();

    public static DriverFactory getInstance() {
        return instance;
    }
    
    public WebDriver getDefaultDriver() {
        String browser = System.getenv("BROWSER");
        if ("chrome".equalsIgnoreCase(browser)) {
            return getChromeDriver();
        } else if ("firefox".equalsIgnoreCase(browser)) {
            return getFirefoxDriver();
        } else if ("internetexplorer".equalsIgnoreCase(browser)) {
            return getInternetExplorerDriver();
        } else if ("phantomjs".equalsIgnoreCase(browser)) {
            return getPhantomJsDriver();
        }
        return getPhantomJsDriver();
    }
    
    private WebDriver getInternetExplorerDriver() {
        // FIXME
        throw new RuntimeException("Not yet implemented");
    }

    public WebDriver getPhantomJsDriver() {  
        String driverPath = System.getenv(PHANTOMJS_DRIVER_PATH);
        if (driverPath.isEmpty()) {
            throw new RuntimeException("La variable " + PHANTOMJS_DRIVER_PATH + " n'est pas définie");
        }
        
        return getPhantomJsDriver(driverPath);
    }

	public WebDriver getPhantomJsDriver(String driverPath) {
		if (driverPath != null && !driverPath.isEmpty()) {
            System.setProperty("phantomjs.binary.path", Paths.get(driverPath, "phantomjs").toString());
        }
        
        return new PhantomJSDriver();
	}
	
    public WebDriver getChromeDriver() {       
        String driverPath = System.getenv(CHROME_DRIVER_PATH);
        if (driverPath.isEmpty()) {
            throw new RuntimeException("La variable " + CHROME_DRIVER_PATH + " n'est pas définie");
        }
        return getChromeDriver(driverPath);
    }

	public WebDriver getChromeDriver(String driverPath) {
		System.setProperty("webdriver.chrome.driver", Paths.get(driverPath, "chromedriver").toString());
        ChromeDriver driver = new ChromeDriver();
        return driver;
	}

    public WebDriver getFirefoxDriver() {
        String driverPath = System.getenv(FIREFOX_DRIVER_PATH);
        if (driverPath.isEmpty()) {
            throw new RuntimeException("La variable " + FIREFOX_DRIVER_PATH + " n'est pas définie");
        }
        return getFirefoxDriver(driverPath);
    }

	WebDriver getFirefoxDriver(String driverPath) {
		System.setProperty("webdriver.gecko.driver", Paths.get(driverPath, "geckodriver").toString());

        RemoteWebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
	}

}
