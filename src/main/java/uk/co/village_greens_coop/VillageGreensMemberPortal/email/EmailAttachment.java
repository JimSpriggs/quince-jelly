package uk.co.village_greens_coop.VillageGreensMemberPortal.email;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;

public class EmailAttachment {
	String attachmentFileName;
	String fullPathAndFileName;
	
	public EmailAttachment(String attachmentFileName, String fullPathAndFileName) {
		super();
		this.attachmentFileName = attachmentFileName;
		this.fullPathAndFileName = fullPathAndFileName;
	}
	public EmailAttachment(Document document) {
		super();
		this.attachmentFileName = document.getFilename();
		this.fullPathAndFileName = "/VillageGreensMembers/documents/" + attachmentFileName;
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
