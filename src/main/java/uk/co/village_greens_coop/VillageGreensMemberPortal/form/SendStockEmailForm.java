package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;

public class SendStockEmailForm {
    
	private Long emailId;
	private String emailPurpose;
	private String emailSubject;
	private String emailBody;

	private Boolean allMembers = Boolean.FALSE;
	private Boolean fullMembers = Boolean.FALSE;
	private Boolean partMembers = Boolean.FALSE;
	private Boolean unpaidMembers = Boolean.FALSE;
	private Boolean overdueMembers = Boolean.FALSE;
	private Boolean committeeMembers = Boolean.TRUE;
	
	public SendStockEmailForm() {
	}
	
	public SendStockEmailForm(StockEmail stockEmail) {
		emailId = stockEmail.getId();
		emailBody = stockEmail.getEmailBody();
		emailSubject = stockEmail.getEmailSubject();
		emailPurpose = stockEmail.getEmailPurpose();
	}
	
	public Boolean getAllMembers() {
		return allMembers;
	}

	public void setAllMembers(Boolean allMembers) {
		this.allMembers = allMembers;
	}

	public Boolean getFullMembers() {
		return fullMembers;
	}

	public void setFullMembers(Boolean fullMembers) {
		this.fullMembers = fullMembers;
	}

	public Boolean getPartMembers() {
		return partMembers;
	}

	public void setPartMembers(Boolean partMembers) {
		this.partMembers = partMembers;
	}

	public Boolean getUnpaidMembers() {
		return unpaidMembers;
	}

	public void setUnpaidMembers(Boolean unpaidMembers) {
		this.unpaidMembers = unpaidMembers;
	}

	public Boolean getOverdueMembers() {
		return overdueMembers;
	}

	public void setOverdueMembers(Boolean overdueMembers) {
		this.overdueMembers = overdueMembers;
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

	public String getEmailPurpose() {
		return emailPurpose;
	}

	public void setEmailPurpose(String emailPurpose) {
		this.emailPurpose = emailPurpose;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public Boolean getCommitteeMembers() {
		return committeeMembers;
	}

	public void setCommitteeMembers(Boolean committeeMembers) {
		this.committeeMembers = committeeMembers;
	}
}
