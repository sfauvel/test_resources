package org.sfvl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Example using a Rule to manage the driver.
 * We need to init driver into the rule and use this driver to init the one into the test.
 */
public class SeleniumLiveOnErrorWithRuleTest {

    private WebDriver driver;
   
    @Rule
    public TakeHandOnError console = new TakeHandOnError() {
        public WebDriver initDriver() {
            DriverFactory factory = DriverFactory.getInstance();
            WebDriver driver = factory.getChromeDriver();
            driver.get("http://localhost:8080/welcome");
            return driver;
        };
    };
        
    @Before
    public void init() {
        driver = console.getDriver();
    }

    @Test
    public void testExecuteSelenium() throws Exception {
        WebElement username = driver.findElement(By.id("username"));
        System.out.println(username.getText());

        WebElement unknown = driver.findElement(By.id("des not exist"));
        System.out.println(unknown.getText());
    }

 

}
