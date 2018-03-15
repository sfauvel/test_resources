package org.sfvl.manualbdd.steps.ko;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;

public class Step {
	@Given("(.*)")
	public void ko(String message) throws Throwable {
		System.out.println(message);
		if (message.startsWith("ERREUR")) {		
			throw new RuntimeException(message);
		}
	}
	
	@After
	public void echec() {
		throw new RuntimeException("Le test ne passe pas");
	}

}
