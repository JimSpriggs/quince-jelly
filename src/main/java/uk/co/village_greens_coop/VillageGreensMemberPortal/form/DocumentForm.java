package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;

public class DocumentForm {

	private Long id;
	
	private Date creationTimestamp;
	
	@Pattern(regexp = "[CUND]")
	private String updateState = "N";

    @Size(max = 100)
	private String filename;
    @Size(max = 200)
	private String description;
	
	public DocumentForm() {
	}
	
	public DocumentForm(Document document) {
		super();
		this.updateState = "U";
		this.id = document.getId();
		this.filename = document.getFilename();
		this.description = document.getDescription();
		this.creationTimestamp = document.getCreationTimestamp();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
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

	
}
