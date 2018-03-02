package org.sfvl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * Take input from the console and execute instruction.
 * On exception, the message is display but we can try another thing.
 * 
 * To use it, just call:
 * SeleniumConsole.run(driver);
 * 
 * It can be done into the teardown method, before closing driver.
 */
public class SeleniumConsole {
    
	private final PrintStream out;

	public SeleniumConsole() {
		this(System.out);
	}
	
	public SeleniumConsole(PrintStream out) {
		this.out = out;
	}

	public static void run(WebDriver driver) {
        run(driver, System.out);
    }
    
    public static void run(WebDriver driver, PrintStream out) {
        new SeleniumConsole(out).giveControleToTheConsole(driver);
    }
    
    void giveControleToTheConsole(WebDriver driver) {
        GroovyShell shell = initGroovyShell(driver);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean readCmdLine = true;
        while (readCmdLine) {
            try {
                String line = in.readLine();
                readCmdLine = !"!".equals(line);
                if (readCmdLine) {
                    execute(shell, line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     */        
    private void execute(GroovyShell shell, String line) {
        try {             
           Object value = shell.evaluate(line);
           out.println(value);
        } catch (Exception e) {
            System.err.println("Line could not be executed:" + line);
            System.err.println(e.getMessage());
        }
    }

    private GroovyShell initGroovyShell(WebDriver driver) {
        CompilerConfiguration config = new CompilerConfiguration();
        addImports(config, By.class);
        
        Binding binding = new Binding();
        binding.setVariable("driver", driver);
        
        return new GroovyShell(this.getClass().getClassLoader(), binding, config);
    }

    private void addImports(CompilerConfiguration config, Class<? extends Object>... classes) {
        ImportCustomizer importCustomizer = new ImportCustomizer();
        for (Class<? extends Object> classToImport : classes) {
            importCustomizer.addImports(classToImport.getName());
        }
        config.addCompilationCustomizers(importCustomizer);
    }
}
