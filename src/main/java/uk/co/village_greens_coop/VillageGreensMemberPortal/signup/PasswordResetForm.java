package uk.co.village_greens_coop.VillageGreensMemberPortal.signup;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ScriptAssert;

@ScriptAssert(
		lang = "javascript",
		script = "_this.repeatPassword.equals(_this.password)",
		message = PasswordResetForm.PASSWORD_MISMATCH)
public class PasswordResetForm {
	public static final String PASSWORD_MISMATCH = "account.password.mismatch.message";
	
	private Long id;
	private String key;
	
    @NotBlank
    @Size(max = 20)
	private String firstName;

    @NotBlank
    @Size(max = 30)
	private String surname;
	
    @NotBlank
    @Email
	private String email;

    @NotBlank
	private String password;

    @NotBlank
	private String repeatPassword;

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
