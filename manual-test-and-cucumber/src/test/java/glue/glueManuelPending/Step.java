package glue.glueManuelPending;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.fr.Etantdonné;

public class Step {
	
	@Before
	public void notExecuted() {
		throw new PendingException("Not executed");
	}
	
	@Etantdonné(".*")
	public void pending() throws Throwable {
	}

}
