package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contact_list")
@NamedQueries({
	@NamedQuery(name = ContactList.FIND_BY_LIST,
		query = "select cl from ContactList cl where cl.id = :id"),
	@NamedQuery(name = ContactList.FIND_CONSENTED_BY_LIST,
		query = "select cl from ContactList cl inner join fetch cl.listSubscribers ls inner join fetch ls.listSubscriberConsents lsc where lsc.marketing = true AND cl.id = :id order by lsc.id")
})
public class ContactList implements java.io.Serializable {

	private static final long serialVersionUID = -2065528089497586759L;

	public static final String FIND_BY_LIST = "ContactList.findByList";
	public static final String FIND_CONSENTED_BY_LIST = "ContactList.findConsentedByList";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "description_tx")
	private String description;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="contactList")
	private Set<ListSubscriber> listSubscribers = new HashSet<ListSubscriber>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ListSubscriber> getListSubscribers() {
		return listSubscribers;
	}

	public void setListSubscribers(Set<ListSubscriber> listSubscribers) {
		this.listSubscribers = listSubscribers;
	}
}
