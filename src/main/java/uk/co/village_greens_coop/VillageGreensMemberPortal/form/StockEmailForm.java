package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;

public class StockEmailForm {
	private Long id;
	private String updateState;
    
	@Size(max = 20)
	private String emailPurpose;
    
	@NotEmpty
	@Size(max = 200)
	private String emailSubject;
	
	@NotEmpty
	private String emailBody;

	public StockEmailForm() {
		updateState = "N";
	}
	
	public StockEmailForm(StockEmail stockEmail) {
		id = stockEmail.getId();
		updateState = "U";
		emailPurpose = stockEmail.getEmailPurpose();
		emailBody = stockEmail.getEmailBody();
		emailSubject = stockEmail.getEmailSubject();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}

	public String getEmailPurpose() {
		return emailPurpose;
	}

	public void setEmailPurpose(String emailPurpose) {
		this.emailPurpose = emailPurpose;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
}
