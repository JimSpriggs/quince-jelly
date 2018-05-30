package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "list_subscriber_consent")
@NamedQuery(name = ListSubscriberConsent.FIND_BY_ID, query = "select lsc from ListSubscriberConsent lsc where lsc.id = :id")
public class ListSubscriberConsent implements java.io.Serializable {

	public static final String FIND_BY_ID = "ListSubscriberConsent.findById";

	private static final long serialVersionUID = 123335083793292519L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "list_subscriber_id")
	private ListSubscriber listSubscriber;

	@Column(name = "marketing_in")
	private Boolean marketing;

	@Column(name = "creation_ts")
	private Date creationTimestamp;

	@Column(name = "update_ts")
	private Date updateTimestamp;

	public ListSubscriberConsent() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ListSubscriber getListSubscriber() {
		return listSubscriber;
	}

	public void setListSubscriber(ListSubscriber listSubscriber) {
		this.listSubscriber = listSubscriber;
	}

	public Boolean getMarketing() {
		return marketing;
	}

	public void setMarketing(Boolean marketing) {
		this.marketing = marketing;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
