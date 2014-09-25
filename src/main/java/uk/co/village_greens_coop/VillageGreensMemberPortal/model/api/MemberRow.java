package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.utils.MemberUtils;

@Component
public class MemberRow {

	private Long id;
	private Long memberNo;
	private String memberStatus;
	private String title;
	private String firstName;
	private String surname;
	private String organisation;
	private String displayName;
	private String email;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressLine4;
	private String postcode;
	private String dob;
	private BigDecimal totalInvestment;
	private Boolean rollCall;
	private Boolean seis;
	private String certificateGenerated;
	private Date certificateSent;
	
	public MemberRow() {
	}
	
	public MemberRow(Member member) {
		this.id = member.getId();
		this.memberNo = member.getMemberno();
		this.title = member.getTitle();
		this.firstName = member.getFirstName();
		this.surname = member.getSurname();
		this.organisation = member.getOrganisation();
		this.email = member.getEmail();
		this.addressLine1 = member.getAddressLine1();
		this.addressLine2 = member.getAddressLine2();
		this.addressLine3 = member.getAddressLine3();
		this.addressLine4 = member.getAddressLine4();
		this.postcode = member.getPostcode();
		this.dob = member.getDob();
		this.totalInvestment = member.getTotalInvestment();
		this.rollCall = member.getRollCall();
		this.seis = member.getSeis();
		this.certificateGenerated = member.getCertificateGenerated() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(member.getCertificateGenerated());
		this.certificateSent = member.getCertificateSent();
		this.memberStatus = member.getMemberStatus();
		setDisplayName();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSurname() {
		return surname;
	}

	public String getOrganisation() {
		return organisation;
	}

	public String getEmail() {
		return email;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getDob() {
		return dob;
	}

	public BigDecimal getTotalInvestment() {
		return totalInvestment;
	}

	public Boolean getRollCall() {
		return rollCall;
	}

	public Boolean getSeis() {
		return seis;
	}

	public String getCertificateGenerated() {
		return certificateGenerated;
	}

	public Date getCertificateSent() {
		return certificateSent;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
		setDisplayName();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		setDisplayName();
	}

	public void setSurname(String surname) {
		this.surname = surname;
		setDisplayName();
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
		setDisplayName();
	}

	private void setDisplayName() {
		this.displayName = MemberUtils.getDisplayName(organisation, firstName, surname);
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setTotalInvestment(BigDecimal totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public void setRollCall(Boolean rollCall) {
		this.rollCall = rollCall;
	}

	public void setSeis(Boolean seis) {
		this.seis = seis;
	}

	public void setCertificateGenerated(String certificateGenerated) {
		this.certificateGenerated = certificateGenerated;
	}

	public void setCertificateSent(Date certificateSent) {
		this.certificateSent = certificateSent;
	}

	public Long getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(Long memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getDisplayName() {
		return displayName;
	}
}
