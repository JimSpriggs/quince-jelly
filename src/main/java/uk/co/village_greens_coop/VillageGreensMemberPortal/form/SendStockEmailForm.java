package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;

public class SendStockEmailForm {
    
	private Long emailId;
	private String emailPurpose;
	private String emailSubject;
	private String emailBody;

	private Boolean allMembers = Boolean.FALSE;
	private Boolean fullMembers = Boolean.FALSE;
	private Boolean fullConsentedMembers = Boolean.FALSE;
	private Boolean partMembers = Boolean.FALSE;
	private Boolean unpaidMembers = Boolean.FALSE;
	private Boolean overdueMembers = Boolean.FALSE;
	private Boolean committeeMembers = Boolean.FALSE;
	private Boolean adhocRecipients = Boolean.TRUE;
	private Boolean mailingList = Boolean.FALSE;
	private String recipients = "";
	private String contactListId = "";

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

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

	public Boolean getFullConsentedMembers() {
		return fullConsentedMembers;
	}

	public void setFullConsentedMembers(Boolean fullConsentedMembers) {
		this.fullConsentedMembers = fullConsentedMembers;
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

	public Boolean getAdhocRecipients() {
		return adhocRecipients;
	}

	public void setAdhocRecipients(Boolean adhocRecipients) {
		this.adhocRecipients = adhocRecipients;
	}

	public Boolean getMailingList() {
		return mailingList;
	}

	public void setMailingList(Boolean mailingList) {
		this.mailingList = mailingList;
	}

	public String getContactListId() {
		return contactListId;
	}

	public void setContactListId(String contactListId) {
		this.contactListId = contactListId;
	}
}
