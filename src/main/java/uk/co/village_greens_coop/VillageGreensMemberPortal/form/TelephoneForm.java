package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation.ScriptAssertFieldError;

@ScriptAssertFieldError.List({
	@ScriptAssertFieldError(
			lang = "javascript",
			script = "!_this.updateState.equals('N') || ( _this.updateState.equals('N') && ( ( _this.telephoneNumber == '' && _this.telephoneType == '' ) || ( _this.telephoneNumber != '' ) ) ) ",
			fieldName = "telephoneNumber"),
	@ScriptAssertFieldError(
			lang = "javascript",
			script = "!_this.updateState.equals('N') || ( _this.updateState.equals('N') && ( ( _this.telephoneNumber == '' && _this.telephoneType == '' ) || ( _this.telephoneType != '' ) ) ) ",
			fieldName = "telephoneType"),
	@ScriptAssertFieldError(
			lang = "javascript",
			script = "!_this.updateState.equals('U') || ( _this.updateState.equals('U') && _this.telephoneNumber != '' )",
			fieldName = "telephoneNumber"),
	@ScriptAssertFieldError( // 
			lang = "javascript",
			script = "!_this.updateState.equals('U') || ( _this.updateState.equals('U') && _this.telephoneType != '' )",
			fieldName = "telephoneType")
})
public class TelephoneForm implements Comparable<TelephoneForm> {

	private Long id;
	
	@Size(max=20)
	private String telephoneNumber;
	
	private String telephoneType;

	@Pattern(regexp = "[CUND]")
	private String updateState = "N";
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public TelephoneForm() {
	}
	
	public TelephoneForm(Long id, String telephoneNumber, String telephoneType) {
		super();
		this.updateState = "U";
		this.id = id;
		this.telephoneNumber = telephoneNumber;
		this.telephoneType = telephoneType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getTelephoneType() {
		return telephoneType;
	}

	public void setTelephoneType(String telephoneType) {
		this.telephoneType = telephoneType;
	}

	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}

	@Override
	public int compareTo(TelephoneForm o) {
		return id.compareTo(o.id);
	}
}
