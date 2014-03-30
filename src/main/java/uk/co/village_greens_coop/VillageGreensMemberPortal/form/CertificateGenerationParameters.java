package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

public class CertificateGenerationParameters {

	private int batchSize = 10;
	private String sendEmailsTo = "jhurst1970@gmail.com";
	
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public String getSendEmailsTo() {
		return sendEmailsTo;
	}
	public void setSendEmailsTo(String sendEmailsTo) {
		this.sendEmailsTo = sendEmailsTo;
	}
	
	
}
