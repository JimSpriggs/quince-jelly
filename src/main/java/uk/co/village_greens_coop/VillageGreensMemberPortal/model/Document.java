package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "document")
@NamedQueries({
	@NamedQuery(name = Document.FIND_BY_ID, query = "select d from Document d where d.id = :id")
})
public class Document implements Comparable, java.io.Serializable {

	public static final String FIND_BY_ID = "DocumentfindById";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "creation_ts")
	private Date creationTimestamp;
	@Column(name = "filename_tx")
	private String filename;
	@Column(name = "description_tx")
	private String description;
	
	public Document() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", creationTimestamp="
				+ creationTimestamp + ", filename=" + filename
				+ ", description=" + description + "]";
	}

	@Override
	public int compareTo(Object o) {
		return (this.id.compareTo(((Document)o).id));
	}
}
