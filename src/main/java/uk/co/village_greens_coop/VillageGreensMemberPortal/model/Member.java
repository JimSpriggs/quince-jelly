package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "member")
@NamedQueries({
	@NamedQuery(name = Member.FIND_BY_SURNAME, query = "select m from Member m where m.surname = :surname"),
	@NamedQuery(name = Member.FIND_BY_STATUS, 
		query = "select m from Member m LEFT JOIN FETCH m.memberTelephones where member_status_cd = :memberStatus order by m.memberno, m.id"),
})
public class Member implements java.io.Serializable {

	public static final String FIND_BY_SURNAME = "Member.findBySurname";
	public static final String FIND_BY_STATUS = "Member.findByMemberStatus";

	@Id
	@GeneratedValue
	private Long id;
	@Column 
	Long memberno;
	@Column
	private String title;
	@Column
	private String firstName;
	@Column
	private String surname;
	@Column
	private String organisation;
	@Column
	private String email;
	@Column
	private String addressLine1;
	@Column
	private String addressLine2;
	@Column
	private String addressLine3;
	@Column
	private String addressLine4;
	@Column
	private String postcode;
	@Column
	private String dob;
	@Column
	private BigDecimal totalInvestment;
	@Column
	private Boolean rollCall;
	@Column
	private Boolean seis;
	@Column 
	private Date certificateGenerated;
	@Column
	private Date certificateSent;
	@Column(name = "member_status_cd")
	private String memberStatus;
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="member")
	private Set<MembershipPayment> membershipPayments;
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="member")
	private Set<MemberTelephone> memberTelephones;
	
	protected Member() {
	}

	public Member(String title, String firstName,
			String surname, String email, 
			String addressLine1, String addressLine2,
			String addressLine3, String addressLine4, String postcode,
			String dob, BigDecimal totalInvestment, Boolean rollCall) {
		super();
		this.title = title;
		this.firstName = firstName;
		this.surname = surname;
		this.email = email;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.addressLine4 = addressLine4;
		this.postcode = postcode;
		this.dob = dob;
		this.totalInvestment = totalInvestment;
		this.rollCall = rollCall;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id	= id;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getDisplayName() {
		if (organisation != null && !organisation.equals("")) {
			return organisation;
		} else {
			String name = firstName;
			if (name != null && !name.equals("")) {
				name = name + " " + surname;
			} else {
				name = surname;
			}
			return name;
		}
	}

	public String getSalutation(boolean formal) {
		String retval = formal ? "Dear " : "Hi ";;
		if (organisation != null && !organisation.equals("")) {
			retval += organisation;
		} else {
			if (formal) {
				retval += String.format("%s %s", title, surname);
			} else {
				retval += firstName;
			}
		}
		return retval;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public BigDecimal getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(BigDecimal totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public Boolean getRollCall() {
		return rollCall;
	}

	public void setRollCall(Boolean rollCall) {
		this.rollCall = rollCall;
	}
	
	public Boolean getSeis() {
		return seis;
	}

	public void setSeis(Boolean seis) {
		this.seis = seis;
	}
	
	public Date getCertificateSent() {
		return certificateSent;
	}

	public void setCertificateSent(Date certificateSent) {
		this.certificateSent = certificateSent;
	}
	
	public Date getCertificateGenerated() {
		return certificateGenerated;
	}

	public void setCertificateGenerated(Date certificateGenerated) {
		this.certificateGenerated = certificateGenerated;
	}

    public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

    public Long getMemberno() {
		return memberno;
	}

	public void setMemberno(Long memberno) {
		this.memberno = memberno;
	}

	public Set<MembershipPayment> getMembershipPayments() {
		return membershipPayments;
	}

	public void setMembershipPayments(Set<MembershipPayment> membershipPayments) {
		this.membershipPayments = membershipPayments;
	}

	public Set<MemberTelephone> getMemberTelephones() {
		return memberTelephones;
	}

	public void setMemberTelephones(Set<MemberTelephone> memberTelephones) {
		this.memberTelephones = memberTelephones;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this)
        		.append("memberno", id)
                .append("firstName", firstName)
                .append("surname", surname)
                .append("organisation", organisation)
                .append("email", email)
                .append("totalInvestment", totalInvestment).toString();
    }
}
