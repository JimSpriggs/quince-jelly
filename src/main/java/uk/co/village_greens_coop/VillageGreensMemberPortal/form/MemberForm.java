package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.utils.MemberUtils;

public class MemberForm {

	private String updateState;
	private Long id;
	private Long memberNo;
	private String memberStatus;
	
    @Size(max = 20)
	private String title;

    @Size(max = 30)
	private String firstName;

    @Size(max = 30)
	private String surname;

    @Size(max = 50)
    private String organisation;
    
	private String displayName;
	
	@Email
    @Size(max = 50)
	private String email;

    @Size(max = 100)
	private String addressLine1;
    @Size(max = 100)
	private String addressLine2;
    @Size(max = 100)
	private String addressLine3;
    @Size(max = 100)
	private String addressLine4;
    @Size(max = 10)
	private String postcode;
    
    @Size(max = 10)
	private String dob;
    
    @NotNull
	private Integer totalInvestment;
    
	private Boolean rollCall;

	private Boolean seis;

	@Valid
	private List<TelephoneForm> telephones = new ArrayList<TelephoneForm>();
	
	@Valid
	private List<PaymentForm> payments = new ArrayList<PaymentForm>();

	public MemberForm() {
		this.updateState = "N";
	}
	
	public MemberForm(Member member) {
		this.updateState = "U";
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
		this.totalInvestment = member.getTotalInvestment().intValue();
		this.rollCall = member.getRollCall();
		this.seis = member.getSeis();
		this.memberStatus = member.getMemberStatus();
		Set<MemberTelephone> memberTelephones = member.getMemberTelephones();
		for (MemberTelephone memberTelephone: memberTelephones) {
			TelephoneForm telephoneForm = new TelephoneForm(
								memberTelephone.getId(),
								memberTelephone.getTelephoneNumber(),
								memberTelephone.getTelephoneType().getTelephoneType());
			this.telephones.add(telephoneForm);
		}
		if (this.telephones.size() > 1) {
			Collections.sort(this.telephones);
		}
		
		// add a new empty telephone to the form, with id 0 
		TelephoneForm tf = new TelephoneForm();
		tf.setId(0L);
		this.telephones.add(tf);
		setDisplayName();

		// Payments
		Set<MembershipPayment> membershipPayments = member.getMembershipPayments();
		for (MembershipPayment membershipPayment: membershipPayments) {
			PaymentForm paymentForm = new PaymentForm(
					membershipPayment.getId(),
					membershipPayment.getPaymentAmount(),
					membershipPayment.getPaymentMethod() != null ?
							membershipPayment.getPaymentMethod().getPaymentMethod() : null,
					membershipPayment.getDueDate(),
					membershipPayment.getReceivedDate());
			this.payments.add(paymentForm);
		}
		if (this.payments.size() > 1) {
			Collections.sort(this.payments);
		}
		
		// add a new empty payment to the form, with id 0 
		PaymentForm pf = new PaymentForm();
		pf.setId(0L);
		this.payments.add(pf);
		
		setDisplayName();
	}

	private void setDisplayName() {
		displayName = MemberUtils.getFullDisplayName(organisation, title, firstName, surname);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public Integer getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(Integer totalInvestment) {
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

	public List<TelephoneForm> getTelephones() {
		return telephones;
	}

	public void setTelephones(List<TelephoneForm> telephones) {
		this.telephones = telephones;
	}

	public List<PaymentForm> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentForm> payments) {
		this.payments = payments;
	}
	
	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}
	
}
