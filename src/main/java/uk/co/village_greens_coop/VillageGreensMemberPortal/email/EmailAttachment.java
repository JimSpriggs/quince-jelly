package uk.co.village_greens_coop.VillageGreensMemberPortal.email;

public class EmailAttachment {
	String attachmentFileName;
	String fullPathAndFileName;
	
	public EmailAttachment(String attachmentFileName, String fullPathAndFileName) {
		super();
		this.attachmentFileName = attachmentFileName;
		this.fullPathAndFileName = fullPathAndFileName;
	}
	public String getAttachmentFileName() {
		return attachmentFileName;
	}
	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}
	public String getFullPathAndFileName() {
		return fullPathAndFileName;
	}
	public void setFullPathAndFileName(String fullPathAndFileName) {
		this.fullPathAndFileName = fullPathAndFileName;
	}
	@Override
	public String toString() {
		return "EmailAttachment [attachmentFileName=" + attachmentFileName
				+ ", fullPathAndFileName=" + fullPathAndFileName + "]";
	}
	
}
