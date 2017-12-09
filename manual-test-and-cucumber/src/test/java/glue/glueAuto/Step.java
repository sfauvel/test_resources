package glue.glueAuto;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;

import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Etantdonné;
import cucumber.api.java.fr.Quand;
import dojo.Appli;

public class Step {
	Appli appli = null;
	@Etantdonné("^un test auto$")
	public void un_test_auto() throws Throwable {
		appli = new Appli();
	}

	@Quand("^il est executé$")
	public void il_est_execute() throws Throwable {
	    appli.setOk();
	}

	@Alors("^il fait un test auto$")
	public void il_fait_un_test_auto() throws Throwable {
	   assertThat(appli.isOk()).isTrue();
	}

}
