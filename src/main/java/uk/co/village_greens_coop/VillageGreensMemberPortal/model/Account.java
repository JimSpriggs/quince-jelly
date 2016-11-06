package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQueries({
	@NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email"),
	@NamedQuery(name = Account.FIND_BY_ID, query = "select a from Account a where a.id = :id"),
})	
public class Account implements java.io.Serializable {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";
	public static final String FIND_BY_ID = "Account.findById";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@Column
	private String firstName;
	
	@Column
	private String surname;
	
	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String password;

	@Column(name = "active")
	private Boolean active = Boolean.FALSE; 
	
	@Column(name = "creation_dt")
	private Date creationDate;
	
	@Column(name = "activation_dt")
	private Date activationDate;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "account_role", 
				joinColumns = { 
					@JoinColumn(
							name = "account_id", 
							nullable = false, 
							updatable = false
					) 
				}, 
				inverseJoinColumns = { 
					@JoinColumn(
							name = "role_id", 
							nullable = false, 
							updatable = false
					) 
				}
		)
	private Set<Role> roles;
	
    protected Account() {
	}
	
	public Account(String firstName, String surname, String email, String password, Role role) {
		this.firstName = firstName;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.creationDate = new Date();
		this.active = Boolean.FALSE;
		this.addRole(role);
	}

	public Long getId() {
		return id;
	}

	
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		if (roles == null) {
			roles = new HashSet<Role>();
		}
		roles.add(role);
	}

	public boolean isActive() {
		return (active!= null && active);
	}
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
}
