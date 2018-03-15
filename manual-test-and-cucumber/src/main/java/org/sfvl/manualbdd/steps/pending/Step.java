package org.sfvl.manualbdd.steps.pending;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class Step {
	
	@Before
	public void notExecuted() {
		throw new PendingException("Not executed");
	}
	
	@Given(".*")
	public void pending() throws Throwable {
	}

}
