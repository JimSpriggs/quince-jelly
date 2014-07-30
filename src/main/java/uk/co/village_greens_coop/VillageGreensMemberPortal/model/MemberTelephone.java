package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "member_telephone")
@NamedQuery(name = MemberTelephone.FIND_BY_ID, query = "select mt from MemberTelephone mt where mt.id = :id")
public class MemberTelephone implements java.io.Serializable {

	public static final String FIND_BY_ID = "MemberTelephone.findById";

	private static final long serialVersionUID = 5827105231655178212L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "telephone_number")
	private String telephoneNumber;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "telephone_type_cd")
	private TelephoneType telephoneType;

	public MemberTelephone() {
	}
	
	public MemberTelephone(Member member, String telephoneNumber,
			TelephoneType telephoneType) {
		super();
		this.member = member;
		this.telephoneNumber = telephoneNumber;
		this.telephoneType = telephoneType;
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

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public TelephoneType getTelephoneType() {
		return telephoneType;
	}

	public void setTelephoneType(TelephoneType telephoneType) {
		this.telephoneType = telephoneType;
	}

}
