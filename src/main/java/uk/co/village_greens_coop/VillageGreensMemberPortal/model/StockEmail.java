package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "stock_email")
@NamedQueries({
	@NamedQuery(name = StockEmail.FIND_BY_PURPOSE, query = "select e from StockEmail e where e.emailPurpose = :emailPurpose"),
	@NamedQuery(name = StockEmail.FIND_BY_ID, query = "select e from StockEmail e where e.id = :id")
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
	@Column(name = "email_subject_tx")
	private String emailSubject;
	
	public StockEmail() {
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

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this)
        		.append("emailPurpose", emailPurpose)
                .append("emailBody", emailBody)
                .append("emailSubject", emailSubject).toString();
    }
}
