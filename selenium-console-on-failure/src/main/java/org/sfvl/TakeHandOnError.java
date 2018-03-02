package org.sfvl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

abstract class TakeHandOnError extends TestWatcher {

    private WebDriver driver;
	private PrintStream outputStream;

    public TakeHandOnError(PrintStream outputStream) {
    	System.out.println("TakeHandOnError.TakeHandOnError()");
		this.outputStream = outputStream;
	}

    public TakeHandOnError() {
    	this(System.out);
	}

    
	public TakeHandOnError(PrintStream printStream, ByteArrayOutputStream outStream) {
		this(printStream);
		System.out.println("TakeHandOnError.TakeHandOnError() " + System.identityHashCode(outStream));
	}

	/**
     * Implements this method to return the driver to use.
     * It will be closed
     * 
     * @return
     */
    public abstract WebDriver initDriver();

    @Override
    protected void starting(Description description) {
        driver = initDriver();
        super.starting(description);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        System.err.println(e.getMessage());
        System.out.println("\nTest is over. You can continue by yourself. Tape ! to exit");
        SeleniumConsole.run(driver, outputStream);
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        driver.close();
    }

    public WebDriver getDriver() {
        return driver;
    }

}