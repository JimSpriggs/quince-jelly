package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;


public class DocumentRow implements java.io.Serializable {

	private static final long serialVersionUID = -4529340961462351940L;

	private Long id;
	private String creationTimestamp;
	private Long creationTimestampMillis;
	private String filename;
	private String description;
	
	public DocumentRow() {
	}

	public DocumentRow(Document doc) {
		this.id = doc.getId();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		this.creationTimestamp = sdf.format(doc.getCreationTimestamp());
		this.creationTimestampMillis = doc.getCreationTimestamp().getTime();
		this.filename = doc.getFilename();
		this.description = doc.getDescription();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(String creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Long getCreationTimestampMillis() {
		return creationTimestampMillis;
	}

	public void setCreationTimestampMillis(Long creationTimestampMillis) {
		this.creationTimestampMillis = creationTimestampMillis;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
