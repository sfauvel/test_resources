package org.sfvl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Example with using before and after to manager driver.
 * 
 * The method after is called before the exception is displayed. 
 * It's a bit difficult to understand wht the script is stopped.  
 */
public class SeleniumLiveOnErrorTest {
   
    WebDriver driver;
    SimpleWebApp webapp = new SimpleWebApp(8080);
    
    @Before
    public void init() throws IOException {
    	webapp.start();
    	
        driver = DriverFactory.getInstance().getDriver();
        driver.get("http://localhost:8080/welcome");
    }
    
    @After
    public void tearDown() {
        // Take controle to the console. Only for developement use. Do not activate in continuous intégration.
        // The error message is not yet displayed. 
       // System.out.println("Test is over. You can continue by yourself. Tape ! to exit");
      //  SeleniumConsole.run(driver);
        driver.close();
        
        webapp.stop();
    }

    @Test
    public void testExecuteSelenium() throws Exception {
    	
        WebElement enterButton = driver.findElement(By.id("enter_button"));
        assertEquals("Enter", enterButton.getText());
    }

 

}
