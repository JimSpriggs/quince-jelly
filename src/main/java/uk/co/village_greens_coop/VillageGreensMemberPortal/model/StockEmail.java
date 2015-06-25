package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "stock_email")
@NamedQueries({
	@NamedQuery(name = StockEmail.FIND_BY_PURPOSE, query = "select e from StockEmail e where e.emailPurpose = :emailPurpose"),
	@NamedQuery(name = StockEmail.FIND_BY_ID, query = "select distinct e from StockEmail e left join fetch e.attachments where e.id = :id")
})
public class StockEmail implements java.io.Serializable {

	public static final String EMAIL_PURPOSE_MEMBER_CERTIFICATE = "MEMBER_CERTIFICATE";
	
	public static final String FIND_BY_PURPOSE = "StockEmailfindByPurpose";
	public static final String FIND_BY_ID = "StockEmailfindById";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "email_purpose_tx")
	private String emailPurpose;
	@Column(name = "email_body_tx")
	private String emailBody;
	@Column(name = "email_html_body_tx")
	private String emailHtmlBody;
	@Column(name = "email_subject_tx")
	private String emailSubject;
	@Column(name = "creation_ts")
	private Date creationTimestamp;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "stock_email_attachment", 
				joinColumns = { 
					@JoinColumn(
							name = "stock_email_id", 
							nullable = false, 
							updatable = false
					) 
				}, 
				inverseJoinColumns = { 
					@JoinColumn(
							name = "document_id", 
							nullable = false, 
							updatable = false
					) 
				}
		)
	private Set<Document> attachments = new TreeSet<Document>();
	
	public StockEmail() {
		creationTimestamp = new Date();
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

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailHtmlBody() {
		return emailHtmlBody;
	}

	public void setEmailHtmlBody(String emailHtmlBody) {
		this.emailHtmlBody = emailHtmlBody;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Set<Document> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Document> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachment(Document document) {
		if (attachments == null) {
			attachments = new HashSet<Document>();
		}
		attachments.add(document);
	}

	
	@Override
    public String toString() {
        return new ToStringBuilder(this)
        		.append("emailPurpose", emailPurpose)
                .append("emailBody", emailBody)
                .append("emailHtmlBody", emailHtmlBody)
                .append("emailSubject", emailSubject)
                .append("creationTimestamp", creationTimestamp).toString();
    }
}
