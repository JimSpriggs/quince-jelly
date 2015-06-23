package uk.co.village_greens_coop.VillageGreensMemberPortal.email;

import java.util.Arrays;

public class EmailDetail {
	private String toAddress;
	private String fromAddress;
	private String fromDisplay;
	private String subject;
	private String template;
	private String html;
	private EmailAttachment[] attachments;
	private String error;

	public EmailDetail() {}
	
	public EmailDetail(String toAddress, String fromAddress,
			String fromDisplay, String subject, String template) {
		super();
		this.toAddress = toAddress;
		this.fromAddress = fromAddress;
		this.fromDisplay = fromDisplay;
		this.subject = subject;
		this.template = template;
		attachments = null;
	}
	
	public EmailDetail(String toAddress, String fromAddress,
			String fromDisplay, String subject, String template,
			EmailAttachment... attachments) {
		super();
		this.toAddress = toAddress;
		this.fromAddress = fromAddress;
		this.fromDisplay = fromDisplay;
		this.subject = subject;
		this.template = template;
		this.attachments = attachments;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromDisplay() {
		return fromDisplay;
	}
	public void setFromDisplay(String fromDisplay) {
		this.fromDisplay = fromDisplay;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public boolean hasAttachments() {
		return (attachments != null && attachments.length > 0);
	}
	public EmailAttachment[] getAttachments() {
		return attachments;
	}
	public void setAttachments(EmailAttachment... attachments) {
		this.attachments = attachments;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return "EmailDetail [toAddress=" + toAddress + ", fromAddress="
				+ fromAddress + ", fromDisplay=" + fromDisplay + ", subject="
				+ subject + ", template=" + template + ", attachments="
				+ Arrays.toString(attachments) + "]";
	}
	
	
}
