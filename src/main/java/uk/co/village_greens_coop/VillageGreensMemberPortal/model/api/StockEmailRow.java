package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;

@Component
public class StockEmailRow implements java.io.Serializable {

	private static final long serialVersionUID = 363393138716458329L;

	private Long id;
	private String emailPurpose;
	private String emailSubject;
	private long creationTimestampMillis;
	private String creationTimestamp;
	
	public StockEmailRow() {}
	
	public StockEmailRow(StockEmail email) {
		this.id = email.getId();
		this.emailPurpose = email.getEmailPurpose();
		this.emailSubject = email.getEmailSubject();
		this.creationTimestampMillis = email.getCreationTimestamp().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		this.creationTimestamp = sdf.format(email.getCreationTimestamp());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getCreationTimestamp() {
		return creationTimestamp;
	}
	public Long getCreationTimestampMillis() {
		return creationTimestampMillis;
	}
}
