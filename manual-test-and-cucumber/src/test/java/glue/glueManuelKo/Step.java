package glue.glueManuelKo;

import cucumber.api.java.After;
import cucumber.api.java.fr.Etantdonné;

public class Step {
	@Etantdonné("(.*)")
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
