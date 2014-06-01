package uk.co.village_greens_coop.VillageGreensMemberPortal.signup;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class ForgottenPasswordForm {

    @NotBlank
    @Email
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
