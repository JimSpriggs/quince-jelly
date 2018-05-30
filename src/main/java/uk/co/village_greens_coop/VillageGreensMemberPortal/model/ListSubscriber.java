package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "list_subscriber")
@NamedQuery(name = ListSubscriber.FIND_BY_UUID, query = "select ls from ListSubscriber ls where ls.uuid = :uuid")
public class ListSubscriber implements java.io.Serializable {

	public static final String FIND_BY_UUID = "ListSubscriber.findByUuid";

	private static final long serialVersionUID = -7205404602741560810L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contact_list_id")
	private ContactList contactList;

	@Column(name = "title")
	private String title;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "uuid")
	private String uuid;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="listSubscriber")
	private Set<ListSubscriberConsent> listSubscriberConsents = new HashSet<>();

	public ListSubscriber() {
	}

	public ListSubscriber(ContactList contactList, String title, String firstName, String lastName, String email) {
		super();
		this.contactList = contactList;
		this.title = title;;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContactList getContactList() {
		return contactList;
	}

	public void setContactList(ContactList contactList) {
		this.contactList = contactList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Set<ListSubscriberConsent> getListSubscriberConsents() {
		return listSubscriberConsents;
	}

	public void setListSubscriberConsents(Set<ListSubscriberConsent> listSubscriberConsents) {
		this.listSubscriberConsents = listSubscriberConsents;
	}
}
