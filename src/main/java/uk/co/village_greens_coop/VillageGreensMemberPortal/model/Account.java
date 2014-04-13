package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email")
public class Account implements java.io.Serializable {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String password;

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
	
	public Account(String email, String password, Role role) {
		this.email = email;
		this.password = password;
		this.addRole(role);
	}

	public Long getId() {
		return id;
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
}
