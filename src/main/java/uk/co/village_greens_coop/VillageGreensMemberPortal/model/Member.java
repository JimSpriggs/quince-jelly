package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "member")
@NamedQuery(name = Member.FIND_BY_SURNAME, query = "select m from Member m where m.surname = :surname")
public class Member implements java.io.Serializable {

	public static final String FIND_BY_SURNAME = "Member.findBySurname";

	@Id
	@GeneratedValue
	private Long memberNo;
	@Column
	private String title;
	@Column
	private String firstName;
	@Column
	private String surname;
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
	private Long totalInvestment;
	@Column
	private Boolean rollCall;
	
    protected Member() {
	}

	public Member(String title, String firstName,
			String surname, String email, 
			String addressLine1, String addressLine2,
			String addressLine3, String addressLine4, String postcode,
			String dob, Long totalInvestment, Boolean rollCall) {
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

	public Long getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(Long memberNo) {
		this.memberNo = memberNo;
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

	public Long getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(Long totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public Boolean getRollCall() {
		return rollCall;
	}

	public void setRollCall(Boolean rollCall) {
		this.rollCall = rollCall;
	}
	
}
