package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "password_reset")
@NamedQueries({
	@NamedQuery(name = PasswordReset.FIND_BY_ACCESS_KEY, query = "select pr from PasswordReset pr where pr.accessKey = :accessKey"),
	@NamedQuery(name = PasswordReset.FIND_ALL_EXPIRED, query = "select pr from PasswordReset pr where pr.creationDate < :cutOffDate")
})
public class PasswordReset implements java.io.Serializable {

	public static final String FIND_BY_ACCESS_KEY = "PasswordReset.findByAccessKey";
	public static final String FIND_ALL_EXPIRED = "PasswordReset.findAllExpired";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@Column(name = "creation_dt")
	private Date creationDate;
	
	@Column(name = "access_key")
	private String accessKey;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account account;
	
    protected PasswordReset() {
	}
	
	public PasswordReset(Account account) {
		this.account = account;
		this.creationDate = new Date();
		this.accessKey = UUID.randomUUID().toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@PreRemove
	public void preRemove() {
		// detach the password reset entity from its parent account, prior to removal
		this.account = null;
	}
}
