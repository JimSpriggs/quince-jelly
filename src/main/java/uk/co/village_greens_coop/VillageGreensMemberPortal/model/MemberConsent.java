package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "member_consent")
@NamedQuery(name = MemberConsent.FIND_BY_ID, query = "select mc from MemberConsent mc where mc.id = :id")
public class MemberConsent implements java.io.Serializable {

	public static final String FIND_BY_ID = "MemberConsent.findById";

	private static final long serialVersionUID = 5877928279818976808L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "marketing_in")
	private Boolean marketing;

	@Column(name = "creation_ts")
	private Date creationTimestamp;

	@Column(name = "update_ts")
	private Date updateTimestamp;

	public MemberConsent() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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
