package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.DocumentRow;

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

	private String emailHtmlBody;

	@Valid
	private List<DocumentRow> availableDocuments = new ArrayList<DocumentRow>();
	
	@Valid
	private List<DocumentRow> selectedDocuments = new ArrayList<DocumentRow>();

	public StockEmailForm() { }
	
	public StockEmailForm(List<DocumentRow> availableDocuments) {
		updateState = "N";
		this.availableDocuments = availableDocuments;
	}
	
	public StockEmailForm(StockEmail stockEmail, 
							List<DocumentRow> availableDocuments,
							List<DocumentRow> selectedDocuments) {
		id = stockEmail.getId();
		updateState = "U";
		emailPurpose = stockEmail.getEmailPurpose();
		emailBody = stockEmail.getEmailBody();
		emailHtmlBody = stockEmail.getEmailHtmlBody();
		emailSubject = stockEmail.getEmailSubject();
		this.availableDocuments = availableDocuments;
		this.selectedDocuments = selectedDocuments;
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

	public String getEmailHtmlBody() {
		return emailHtmlBody;
	}

	public void setEmailHtmlBody(String emailHtmlBody) {
		this.emailHtmlBody = emailHtmlBody;
	}

	public List<DocumentRow> getAvailableDocuments() {
		return availableDocuments;
	}

	public void setAvailableDocuments(List<DocumentRow> availableDocuments) {
		this.availableDocuments = availableDocuments;
	}

	public List<DocumentRow> getSelectedDocuments() {
		return selectedDocuments;
	}

	public void setSelectedDocuments(List<DocumentRow> selectedDocuments) {
		this.selectedDocuments = selectedDocuments;
	}

}
